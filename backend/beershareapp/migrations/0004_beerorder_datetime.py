# Generated by Django 3.2.3 on 2021-05-29 22:04

from django.db import migrations, models
import django.utils.timezone


class Migration(migrations.Migration):

    dependencies = [
        ('beershareapp', '0003_auto_20210529_2203'),
    ]

    operations = [
        migrations.AddField(
            model_name='beerorder',
            name='datetime',
            field=models.DateTimeField(auto_now_add=True, default=django.utils.timezone.now),
            preserve_default=False,
        ),
    ]
