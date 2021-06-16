import django_tables2 as tables


class BeerTable(tables.Table):
    brand = tables.Column()
    type = tables.Column()
    liter = tables.Column()
    country = tables.Column()

    class Meta:
        orderable = False
        attrs = {
            "class": "table table-sm table-hover",
            "thead": {
                "class": "thead-dark"
            },
        }
        row_attrs = {
        }


class BeerCellarTable(tables.Table):
    name = tables.Column()
    latitude = tables.Column()
    longitude = tables.Column()
    address = tables.Column()
    owner = tables.Column()

    class Meta:
        orderable = False
        attrs = {
            "class": "table table-sm table-hover",
            "thead": {
                "class": "thead-dark"
            },
        }
        row_attrs = {
        }


class BeerOrderTable(tables.Table):
    amount = tables.Column()
    status = tables.Column()
    datetime = tables.Column()
    beerCellar = tables.Column()
    beer = tables.Column()
    user = tables.Column()

    class Meta:
        orderable = False
        attrs = {
            "class": "table table-sm table-hover",
            "thead": {
                "class": "thead-dark"
            },
        }
        row_attrs = {
        }
