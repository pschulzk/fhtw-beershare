from django.db import models
from django.contrib.auth.models import User


class Address(models.Model):
    address = models.CharField(
        "Address",
        max_length=50,
    )

    zip_code = models.CharField(
        "ZIP / Postal code",
        max_length=12,
    )

    city = models.CharField(
        "City",
        max_length=50,
    )

    country = models.CharField(
        "Country",
        max_length=50
    )


class Beer(models.Model):
    brand = models.CharField(
        max_length=30
    )
    type = models.CharField(
        max_length=30
    )
    liter = models.DecimalField(
        max_digits=4,
        decimal_places=2
    )
    country = models.CharField(
        max_length=30
    )
    user = models.ForeignKey(
        User,
        on_delete=models.CASCADE, null=True
    )


class BeerCellar(models.Model):
    name = models.CharField(
        max_length=30
    )
    latitude = models.DecimalField(     # format:  ##.######
        max_digits=8,
        decimal_places=6
    )
    longitude = models.DecimalField(    # format: ###.######
        max_digits=9,
        decimal_places=6
    )
    address = models.ForeignKey(
        Address,
        on_delete=models.PROTECT
    )
    owner = models.ForeignKey(
        User,
        on_delete=models.CASCADE
    )


class BeerCellarEntry(models.Model):
    amount = models.IntegerField()
    datetime = models.DateTimeField()
    beerCellar = models.ForeignKey(
        BeerCellar,
        on_delete=models.CASCADE
    )
    beer = models.ForeignKey(
        Beer,
        on_delete=models.PROTECT
    )


class BeerOrder(models.Model):

    class Status(models.IntegerChoices):
        PLACED = 1, 'Placed'
        ACCEPTED = 2, 'Accepted'
        DECLINED = 3, 'Declined'
        DONE = 4, 'Done'

    amount = models.PositiveIntegerField()
    status = models.PositiveSmallIntegerField(
        choices=Status.choices
    )
    beerCellarEntry = models.ForeignKey(
        BeerCellarEntry,
        on_delete=models.CASCADE
    )
    user = models.ForeignKey(
        User,
        on_delete=models.CASCADE
    )
