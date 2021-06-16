from django.urls import path

from . import views

urlpatterns = [
    # Frontend routes
    path('', views.BeerTableView.as_view(), name='beershare-view-home'),
    path('beer', views.BeerTableView.as_view(), name='beershare-view-beer'),
    path('beercellar', views.BeerCellarTableView.as_view(), name='beershare-view-beercellar'),
    path('beerorder', views.BeerOrderTableView.as_view(), name='beershare-view-beerorder'),

    # API routes
    path('api/v1/auth/', views.auth, name='beershare-auth'),

    path('api/v1/beer/', views.BeerList.as_view(), name='beershare-beer'),
    path('api/v1/beer/<int:pk>', views.BeerDetail.as_view(), name='beershare-beer-detail'),

    path('api/v1/beercellar/', views.BeerCellarList.as_view(), name='beershare-beercellar'),
    path('api/v1/beercellar/<int:pk>', views.BeerCellarDetail.as_view(), name='beershare-beercellar-detail'),

    path('api/v1/nearbybeercellar/', views.NearbyBeerCellarList.as_view(), name='beershare-nearbybeercellar'),

    path('api/v1/beercellarentry/', views.BeerCellarEntryList.as_view(), name='beershare-beercellarentry'),
    path('api/v1/beercellarentry/<int:pk>', views.BeerCellarEntryDetail.as_view(),
         name='beershare-beercellarentry-detail'),

    path('api/v1/absolutbeercellarentry/', views.BeerCellarEntryAbsoluteCreate.as_view(),
         name='beershare-absolutbeercellarentry'),

    path('api/v1/beerorder/', views.BeerOrderList.as_view(), name='beershare-beerorder'),
    path('api/v1/beerorder/<int:pk>', views.BeerOrderDetail.as_view(), name='beershare-beerorder-detail'),
]
