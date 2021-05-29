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

    class Meta:
        model = models.BeerCellarEntry
        fields = ['id', 'amount', 'datetime', 'beerCellar', 'beer']


class BeerOrderSerializer(serializers.ModelSerializer):
    buyer = serializers.ReadOnlyField(source='user.username')

    class Meta:
        model = models.BeerOrder
        fields = ['id', 'amount', 'status', 'beerCellarEntry', 'buyer']
