# Generated by Django 3.2.3 on 2021-06-03 16:03

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('beershareapp', '0004_beerorder_datetime'),
    ]

    operations = [
        migrations.AlterField(
            model_name='address',
            name='address',
            field=models.CharField(max_length=255, verbose_name='Address'),
        ),
    ]
