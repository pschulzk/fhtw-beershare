from django.db.models import Sum
from rest_framework import serializers
from . import models


class AddressSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.Address
        fields = ['address', 'zip_code', 'city', 'country']


class BeerSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.Beer
        fields = ['id', 'brand', 'type', 'liter', 'country']


class BeerCellarSerializer(serializers.ModelSerializer):
    address = AddressSerializer()
    owner = serializers.ReadOnlyField(source='owner.username')

    class Meta:
        model = models.BeerCellar
        fields = ['id', 'name', 'latitude', 'longitude', 'address', 'owner']

    def create(self, validated_data):
        # create nested address
        address_data = validated_data['address']
        try:
            address = models.Address.objects.get(**address_data)
        except models.Address.DoesNotExist:
            address = models.Address.objects.create(**address_data)

        validated_data['address'] = address
        return super().create(validated_data)

    def update(self, instance, validated_data):
        address_data = validated_data.pop('address')
        for attr, value in address_data.items():
            setattr(instance.address, attr, value)

        for attr, value in validated_data.items():
            setattr(instance, attr, value)

        instance.address.save()
        instance.save()
        return instance


class BeerCellarEntrySerializer(serializers.ModelSerializer):
    class UserBeerCellarField(serializers.PrimaryKeyRelatedField):
        def get_queryset(self):
            return models.BeerCellar.objects.filter(owner=self.context['request'].user)

    beerCellar = UserBeerCellarField()  # only show cellars which belongs to the current user
    beerName = serializers.ReadOnlyField(source="beer.beer_name")

    class Meta:
        model = models.BeerCellarEntry
        fields = ['id', 'amount', 'datetime', 'beerCellar', 'beer', 'beerName']


class AbsoluteBeerCellarEntrySerializer(BeerCellarEntrySerializer):
    """
    Special serializer to update the amount with an absolute value and not the difference
    """

    class Meta:
        model = models.BeerCellarEntry
        fields = ['amount', 'beerCellar', 'beer']

    def create(self, validated_data):
        absolute_amount = int(validated_data['amount'])
        cellar = validated_data['beerCellar']
        beer = validated_data['beer']

        total_amount = models.BeerCellarEntry.objects.filter(beerCellar=cellar, beer=beer).aggregate(a=Sum('amount'))
        validated_data['amount'] = f"{absolute_amount - int(total_amount['a'])}"

        return super().create(validated_data)


class BeerOrderSerializer(serializers.ModelSerializer):

    buyer = serializers.ReadOnlyField(source='user.username')
    beerName = serializers.ReadOnlyField(source="beer.beer_name")

    class Meta:
        model = models.BeerOrder
        fields = ['id', 'amount', 'status', 'datetime', 'beerCellar', 'beer', 'buyer', 'beerName']


# special
# BeerCellar details with all aggregated (amount) BeerCellarEntries
class BeerCellarDetailSerializer(BeerCellarSerializer):
    entries = serializers.SerializerMethodField()

    @staticmethod
    def get_entries(cellar):
        entries = models.BeerCellarEntry.objects.filter(beerCellar=cellar) \
            .values("beer") \
            .annotate(amount=Sum('amount'))

        for entry in entries:
            beer = models.Beer.objects.get(id=entry['beer'])
            entry['beerName'] = beer.beer_name

        return entries

    class Meta:
        model = models.BeerCellar
        fields = ['id', 'name', 'latitude', 'longitude', 'address', 'owner', 'entries']
