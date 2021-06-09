from django.db.models import F, Sum
from django.db.models.functions import Radians, Power, Cos, Sin, ATan2, Sqrt

from . import models
from . import serializers
from . import permissions

from rest_framework import generics
from rest_framework.decorators import api_view, permission_classes
from rest_framework.response import Response
from rest_framework import permissions as rest_permissions
from rest_framework import serializers as rest_serializers


@api_view(['GET'])
@permission_classes([rest_permissions.IsAuthenticated])
def auth(request):
    return Response({"message": f"hello {request.user}"})


# Beer
class BeerList(generics.ListCreateAPIView):
    queryset = models.Beer.objects.all()
    serializer_class = serializers.BeerSerializer


class BeerDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = models.Beer.objects.all()
    serializer_class = serializers.BeerSerializer


# BeerCellar
class BeerCellarList(generics.ListCreateAPIView):
    permission_classes = [rest_permissions.IsAuthenticated]
    serializer_class = serializers.BeerCellarSerializer

    def get_queryset(self):
        return models.BeerCellar.objects.filter(owner=self.request.user)

    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)


class NearbyBeerCellarList(generics.ListAPIView):
    permission_classes = [rest_permissions.IsAuthenticated]
    serializer_class = serializers.BeerCellarSerializer

    def get_queryset(self):
        # based on https://newbedev.com/how-to-filter-a-django-model-with-latitude-and-longitude-coordinates-that-fall-within-a-certain-radius

        current_lat = float(self.request.query_params.get('latitude', 48.23953))
        current_long = float(self.request.query_params.get('longitude', 16.377255))
        distance = self.request.query_params.get('distance', 1)

        dlat = Radians(F('latitude') - current_lat)
        dlong = Radians(F('longitude') - current_long)

        a = (Power(Sin(dlat / 2), 2) + Cos(Radians(current_lat))
             * Cos(Radians(F('latitude'))) * Power(Sin(dlong / 2), 2))

        c = 2 * ATan2(Sqrt(a), Sqrt(1 - a))
        d = 6371 * c

        return models.BeerCellar.objects.annotate(distance=d).order_by('distance').filter(distance__lt=distance)

    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)


class BeerCellarDetail(generics.RetrieveUpdateDestroyAPIView):
    permission_classes = [rest_permissions.IsAuthenticated, permissions.IsOwnerOrReadOnly]
    serializer_class = serializers.BeerCellarDetailSerializer
    queryset = models.BeerCellar.objects.all()


# BeerCellarEntry
class BeerCellarEntryList(generics.ListCreateAPIView):
    permission_classes = [rest_permissions.IsAuthenticated]
    serializer_class = serializers.BeerCellarEntrySerializer

    def get_queryset(self):
        return models.BeerCellarEntry.objects.filter(beerCellar__owner=self.request.user)


class BeerCellarEntryDetail(generics.RetrieveUpdateDestroyAPIView):
    permission_classes = [rest_permissions.IsAuthenticated]
    serializer_class = serializers.BeerCellarEntrySerializer

    def get_queryset(self):
        return models.BeerCellarEntry.objects.filter(beerCellar__owner=self.request.user)


class BeerCellarEntryAbsoluteCreate(generics.CreateAPIView):
    permission_classes = [rest_permissions.IsAuthenticated]
    serializer_class = serializers.AbsoluteBeerCellarEntrySerializer

    def get_queryset(self):
        return models.BeerCellarEntry.objects.filter(beerCellar__owner=self.request.user)


# BeerOrder
class BeerOrderList(generics.ListCreateAPIView):
    permission_classes = [rest_permissions.IsAuthenticated]
    serializer_class = serializers.BeerOrderSerializer

    def get_queryset(self):
        # return entries as seller & as buyer
        return models.BeerOrder.objects.filter(beerCellar__owner=self.request.user) | \
                   models.BeerOrder.objects.filter(user=self.request.user)

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)


class BeerOrderDetail(generics.RetrieveUpdateDestroyAPIView):
    permission_classes = [rest_permissions.IsAuthenticated]
    serializer_class = serializers.BeerOrderSerializer

    def get_queryset(self):
        # return entries as seller & as buyer
        return models.BeerOrder.objects.filter(beerCellar__owner=self.request.user) | \
                   models.BeerOrder.objects.filter(user=self.request.user)

    def perform_update(self, serializer):

        if serializer.validated_data.get('status') == models.BeerOrder.Status.DONE:
            # reduce beer amount
            beer_cellar = serializer.validated_data.get('beerCellar')
            beer = serializer.validated_data.get('beer')
            total_amount = models.BeerCellarEntry.objects.filter(beerCellar=beer_cellar, beer=beer) \
                .aggregate(a=Sum('amount'))

            amount = serializer.validated_data.get('amount')
            if amount <= int(total_amount['a']):
                models.BeerCellarEntry(amount=-amount, beerCellar=beer_cellar, beer=beer).save()
            else:
                raise rest_serializers.ValidationError({"detail": "not enough beer"})

        super().perform_update(serializer)
