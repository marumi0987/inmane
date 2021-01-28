# inmane
プロジェクト研修用アプリケーション

## Branch構成ルール
```
master
develop
feature/hoge1
```

この基本構成を各人の名前以下に作成して使用する  

ex)  

```
hirabayashi/develop
           /feature/hoge1
                   /hoge2
```

## Docker環境(MySQL,SonarQube)
## Docker DesktopのInstall
[Docker Desktop](https://www.docker.com/products/docker-desktop "Download Stable")からStable版を取得、Installして下さい

### 起動
```
inmane\docker\bin\start.bat
```

### 停止
```
inmane\docker\bin\stop.bat
```

### プログラムコードの静的解析実行
```
.\gradlew clean sonarqube
```

### SonarQube画面の表示
[http://localhost:9000/](http://localhost:9000/)  
ID:admin  
Pass:admin  

### コンテナへAttach
```
inmane\docker\bin\attach-inmane-db.bat
inmane\docker\bin\attach-sonarqube.bat
```

### MySQL＆SonarQubeデータの初期化
dockerコンテナを停止  

```
inmane\docker\bin\stop.bat
```

下記ディレクトリ配下のファイルをエクスプローラー等で削除  
inmane\docker\inmane-db\data  
dockerコンテナを起動

```
inmane\docker\bin\start.bat
```

上記内容を実行するbatファイル  

```
inmane\docker\bin\init-data.bat
```

## lombok設定
[Download](https://projectlombok.org/download)  
[EclipseへのInstall方法](https://projectlombok.org/setup/eclipse)  

lombok.jarのダブルクリックで起動しない場合
```
java -jar lombok.jar
```

## ssh client
[putty](https://www.chiark.greenend.org.uk/~sgtatham/putty/latest.html "Download Stable")

## Eclipse Plugins
TODO
