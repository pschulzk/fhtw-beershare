# fhtw-beershare

## Description
Some practice project for Android and iOS development containing:
* Python/Django backend
* Android/Kotlin (SDK v30) mobile app frontend
* iOS/Swift (Xcode 9.3-compatible) mobile app frontend

## Prerequisites
* Python 3.x
* Android Studio
* Xcode
* PostgreSQL database

## How tun run
Configure database connection at `backend/beershare/settings.py`:
```
DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.postgresql_psycopg2',
        'NAME': 'u103484db6',
        'USER': 'u103484db6',
        'PASSWORD': '0.jtp4z6fvng',
        'HOST': 'e68579-pgsql.services.easyname.eu',
        'PORT': '',
    }
}
```

Clone repository:

```$ git clone https://github.com/pschulzk/fhtw-beershare```

Navigate into backend application folder:

```$ cd fhtw-beershare/backend```

Install Python dependencies:

```$ pip3 install -r requirements.txt```

Emulate application environment:

```$ python3 -m venv .venv/```

Start backend application server:

```$ python3 manage.py runserver 0.0.0.0:8000```

Then start Android Studio or Xcode and run preview application.
