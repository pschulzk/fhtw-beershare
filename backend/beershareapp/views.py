from . import models
from . import serializers

from rest_framework import generics
from rest_framework import permissions as rest_permissions


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


class BeerCellarDetail(generics.RetrieveUpdateDestroyAPIView):
    permission_classes = [rest_permissions.IsAuthenticated]
    serializer_class = serializers.BeerCellarSerializer
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


# BeerOrder
class BeerOrderList(generics.ListCreateAPIView):
    permission_classes = [rest_permissions.IsAuthenticated]
    serializer_class = serializers.BeerOrderSerializer

    def get_queryset(self):
        return models.BeerOrder.objects.filter(beerCellarEntry__beerCellar__owner=self.request.user)

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)


class BeerOrderDetail(generics.RetrieveUpdateDestroyAPIView):
    permission_classes = [rest_permissions.IsAuthenticated]
    serializer_class = serializers.BeerOrderSerializer

    def get_queryset(self):
        return models.BeerOrder.objects.filter(beerCellarEntry__beerCellar__owner=self.request.user)
