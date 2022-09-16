# ServiceSample

This is a sample of foreground services on Android.

Since activities have a chance to be stopped by the os when it goes on background, Foreground services can run longer than activity. 

In this application, a foreground service collects location information and shows it as notification. An activity connects the service and gets location information.

This application requires location permission. Please add it manually. 
There is the setting page at Configuration > Application & Notification > ServiceSample > Permission (on Android 11)

This application uses [deploygate](https://deploygate.com/) for remote debugging,
which is not in effect unless installing app from the deploygate application.

----

これは、Android のフォアグラウンド サービスのサンプルです。

アクティビティは、バックグラウンドになると OS によって停止される可能性があるため、フォアグラウンド サービスはアクティビティよりも長く実行できます。

このアプリでは、フォアグラウンドサービスが位置情報を収集し、それを通知として表示します。 アクティビティはサービスに接続し、位置情報を取得します。

このアプリケーションには位置情報の許可が必要です。 手動で追加してください。
Configuration > Application & Notification > ServiceSample > Permission に設定ページがあります (Android 11 の場合)

このアプリケーションは、リモート デバッグに [deploygate](https://deploygate.com/) を使用し、
deploygate アプリケーションからアプリをインストールしない限り、これは有効ではありません。

References / 参考

- Foreground services https://developer.android.com/guide/components/foreground-services?hl=ja
