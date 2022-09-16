# WorkManagerSample

This is a sample of work manager on Android.

In this application, a worker works following:

- it runs periodically.
- it binds a foreground service.
- it makes text message including current date and passes it to the service. The service shows it as a notification.

To get a reference of foreground service in a Worker class,
```bindService()``` must be done before ```doWork()```
because binding a service is executed in asynchronous way and a service reference may not be available just after ```bindService()```. 
In this application, the worker(SampleWorker) binds a service in its initializing.


----

これは、Android の ワーカーマネージャのサンプルです。

このアプリでは、ワーカーは次のように動作します。

- 定期的に実行します。
- フォアグラウンドサービスにバインドします。
- 現在時刻を含む文字列をサービスに渡します。サービスは文字列を通知として表示します。

ワーカークラス内で、サービスの参照を取得するためには、```doWork()``` の前に ```bindService()``` を実行する必要があります。サービスへのバインドは非同期で行われるため、```bindService()```の直後では、サービスの参照は得られない可能性があります。
このアプリでは、ワーカー(SampleWorker) はその初期化時にサービスにバインドします。


References / 参考

- WorkManager でタスクのスケジュールを設定する
    https://developer.android.com/topic/libraries/architecture/workmanager?hl=ja

- Android how do I wait until a service is actually connected?
   https://stackoverflow.com/questions/3055599/android-how-do-i-wait-until-a-service-is-actually-connected
