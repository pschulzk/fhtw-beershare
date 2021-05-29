from django.urls import path

from . import views

urlpatterns = [
    path('api/v1/beer/', views.BeerList.as_view(), name='beershare-beer'),
    path('api/v1/beer/<int:pk>', views.BeerDetail.as_view(), name='beershare-beer-detail'),

    path('api/v1/beercellar/', views.BeerCellarList.as_view(), name='beershare-beercellar'),
    path('api/v1/beercellar/<int:pk>', views.BeerCellarDetail.as_view(), name='beershare-beercellar-detail'),

    path('api/v1/beercellarentry/', views.BeerCellarEntryList.as_view(), name='beershare-beercellarentry'),
    path('api/v1/beercellarentry/<int:pk>', views.BeerCellarEntryDetail.as_view(),
         name='beershare-beercellarentry-detail'),

    path('api/v1/beerorder/', views.BeerOrderList.as_view(), name='beershare-beerorder'),
    path('api/v1/beerorder/<int:pk>', views.BeerOrderDetail.as_view(), name='beershare-beerorder-detail'),
]
