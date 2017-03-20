---

# Android App with RxJava

## Andrea Maglie

---

# Ing. Andrea Maglie

**Senior Android Developer @ Musement**

**Organizer of GDG Venezia**

*andrea.maglie@gmail.com*

*@TechIsFun*

*www.andreamaglie.com*

*+AndreaMaglie*

---

# Imperative vs functional

<!-- 
Nella programmazione funzionale non abbiamo una sequenza di passi da eseguire ma una funzione (in questo caso x mod 2 == 0) che viene passata come parametro ad una altra funzione (where()) la quale viene applicata all oggetto input. L annotazione "freccia verso destra" significa: applica la funzione f(x) (parte destra dell espressione) alla variabile x (parte sinistra dell espressione)
-->

**Imperative programming**
```java
List<Integer> input = Arrays.asList(1, 2, 3, 4, 5);  
 
List<Integer> output = new ArrayList<>();
        
for (Integer x : input) {
   if (x % 2 == 0) {
     output.add(x);
   }
}
```

{.column}

**Functional programming**
```java
var output = input.where( x -> x % 2 == 0);
```

---

# Lambda expressions
```java
var output = input.where( x -> x % 2 == 0);
```
- anonymous functions
- indicated with **input -> function()**


---

# Lambda expression example: click listeners
```java
Button button = ...
button.setOnClickListener(
    new OnClickListener() {
        @Override
        public void onClick() {
            doSomething();
        }        
});
```

```java
button.setOnClickListener( () -> doSomething() )
```

---

# Lambda expression

* supported from Java 8
* for Java 6 and 7 use `retrolambda`

---

# Reactive

<!--
Una sequenza di eventi puo essere costituita dal input di un utente (click su un pulsante), una risposta da una richiesta ad una API, elementi contenuti in una Collection o anche un singolo oggetto
-->

## Just think about a stream of data, or a flow of events

---

# Going reactive with Observer pattern
<!--
Lo scopo del Observer Pattern è di definire una dipendenza 1-a-molti tra oggetti in modo che quando un oggetto cambia stato, tutte le sue dipendenze vengono notificate e aggiornate automaticamente 
-->
```java
class Subject extends java.util.Observable {
 
   public void doWorkAndNotify() {
     Object result = doWork();
     notifyObservers(result);
   }
}

class MyObserver implements Observer { 
   public void update(Observable obs, Object item) {
     doSomethingWith(item)
  } 
}
```

{.column}

```java
MyObserver myObserver = new MyObserver()
 Subject subject = new Subject();
 subject.addObserver(myObserver);
 subject.doWorkAndNotify();
```

---
 
# ReactiveX: A combination of the best ideas from the Observer pattern, the Iterator pattern and functional programming {.big}

[http://reactivex.io](http://reactivex.io/)

---

# RxJava: A library that implements the concepts of ReactiveX in Java {.big}

Available also for other languages: C++ (RxCpp), C# (Rx.NET), Unity (UniRx), JavaScript (RxJS), Scala (RxScala), Clojure (RxClojure), Ruby (Rx.rb), Python (RxPY), Groovy (RxGroovy), JRuby (RxJRuby), Kotlin (RxKotlin), Swift (RxSwift), PHP (RxPHP)

---

# Android Weather App

* listen for user's location
* call DarkSky APIs

---

# Get user location: the *callbacks* way

```java
// Acquire a reference to the system Location Manager
LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
LocationListener locationListener = new LocationListener() {
    public void onLocationChanged(Location location) {
      // Called when a new location is found by the network location provider.
      makeUseOfNewLocation(location);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {}

    public void onProviderDisabled(String provider) {}
  };

// Register the listener with the Location Manager to receive location updates
locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
```
---

# Get user's location: the *Reactive* way

```java
RxLocation rxLocation = new RxLocation(getApplicationContext());

LocationRequest locationRequest = LocationRequest.create()
    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    .setNumUpdates(1);

rxLocation.location().updates(locationRequest)
    .subscribe(location -> doSomethingWithLocation(location))
```

---

# Create Retrofit service

```java
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.darksky.net")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    mDarkSkyApi = retrofit.create(DarkSkyApi.class);
```

---

# Concatenate requests

```java
rxLocation.location().updates(locationRequest)
    .flatMap(location -> {
        return mDarkSkyApi.forecast(apiKey, location.getLatitude(), location.getLongitude());
    })
    .subscribe(weatherDataJson -> {
        String currentWeather = weatherDataJson.get("currently").getAsJsonObject()
                .get("summary").getAsString();
        mTextView.setText(currentWeather);
    });
```

---

# The flatMap operator

![](http://reactivex.io/documentation/operators/images/flatMap.c.png)

---

# The filter operator

![](http://reactivex.io/documentation/operators/images/filter.png)

---

# The startWith operator

![](http://reactivex.io/documentation/operators/images/startWith.png)

---

# Multithreading and Schedulers

```java
rxLocation.location().updates(locationRequest)
    .flatMap(location -> {
        return mDarkSkyApi.forecast(apiKey, location.getLatitude(), location.getLongitude())
                .subscribeOn(Schedulers.io());
    })
    
    .subscribeOn(Schedulers.io())
    
    .subscribe(weatherDataJson -> {
        String currentWeather = weatherDataJson.get("currently").getAsJsonObject()
                .get("summary").getAsString();
        mTextView.setText(currentWeather);
    });
```

---

# Multithreading and Schedulers

```java
rxLocation.location().updates(locationRequest)
    .flatMap(location -> {
        return mDarkSkyApi.forecast(apiKey, location.getLatitude(), location.getLongitude())
                .subscribeOn(Schedulers.io());
    })
    
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    
    .subscribe(weatherDataJson -> {
        String currentWeather = weatherDataJson.get("currently").getAsJsonObject()
                .get("summary").getAsString();
        mTextView.setText(currentWeather);
    });
```

---

# Cancel a subscription

```java
mSubscription = rxLocation.location().updates(locationRequest)
    ...
    .subscribe(weatherDataJson -> {
        String currentWeather = weatherDataJson.get("currently").getAsJsonObject()
                .get("summary").getAsString();
        mTextView.setText(currentWeather);
    });

mSubscription.unsubscribe();
```

---

# Creating observables

* `Observable.just("single item")`
* `Observable.range(1, 10)`
* `Observable.interval(1, TimeUnit.SECONDS)`
* `Observable.empty()`

---

# Creating observables

```java
Observable.create(
  new Observable.OnSubscribe<String>() {
    @Override
    public void call(Subscriber<? super String> observer) {
        observer.onNext("first item");
        observer.onNext("second item");
        observer.onCompleted();
    }
  }
)
```
---

# Creating observables: defer

```java
Person person = new Person();
Observable<String> nameObservable = Observable.just(person.getName());

person.setName("Bob");

nameObservable.subscribe( name -> System.out.println(name) );
```

---

# Creating observables: defer

```java
Person person = new Person();
Observable<String> nameObservable = 
    Observable.defer(() -> Observable.just(person.getName()));

person.setName("Bob");

nameObservable.subscribe( name -> System.out.println(name) );
```

---

# Hot or cold?

## Cold observables

* rxLocation.location().updates(locationRequest)
* mDarkSkyApi.forecast(...)

---

# Hot or cold?

## Example of hot observable

```java
RxView.clicks(mButton)
    .subscribe(new Action1<Void>() {
        @Override
        public void call(Void click) {
            // do something;
        }
    });
```

---

# Hot or cold?

## Example: avoid double clicks

```java
RxView.clicks(mButton)
    .throttleFirst(200, TimeUnit.MILLISECONDS)
    .subscribe(new Action1<Void>() {
        @Override
        public void call(Void click) {
            // do something;
        }
    });
```

---

# onNext, onCompleted, onError

```java
Observable<String> observable = ...
observable.subscribe(
    new Subscriber<String>() {
        @Override public void onNext(String nextValue) {
            // called every time an item is emitted
        }

        @Override public void onError(Throwable e) {
            // called if exception is thrown in the observable chain
        }

        @Override public void onCompleted() {
            // called when sequence is completed (for finite sequences)
        });
```

---

# Test observables

```java
Observable<String> observable = Observable.just("item");

TestSubscriber<String> testSubscriber = new TestSubscriber<>();

observable.subscribe(testSubscriber);

testSubscriber.assertNoErrors();
testSubscriber.assertCompleted();
testSubscriber.assertReceivedOnNext(Arrays.asList("item"));
```

---

# RxJava: Q&A {.big}

---

# Thank you! {.big}

## Andrea Maglie

---