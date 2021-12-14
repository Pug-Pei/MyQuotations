# 制作背景
このアプリは書籍のお気に入りの名言を記録する名言記録するものです。  
本の中には「これはいい言葉だな～」と思う名言がたまに出てきます。  
そういう一文があるページは、長年ページを折り曲げてみたり線を引いて見つけやすくする工夫ができますが、  
これらの方法には少し問題点があります。  

**問題点1. 読み返したい文章がなかなか見つからない**  
→折り目と線だけでは、読み返したい時に探している名言がなかなか見つけられない。

**問題点2. 読み返さない**  
→線を引いただけで満足してしまい、結局読み返さないまま本棚で眠ったままのことが多い。

これらの原因は**文章が整理されおらず、それを読み返す習慣がないこと**です。  
この問題点を解決するアプリを開発することにしました。

主なコンセプトは以下の2つです。
* タイトルごとに文章を記録できる(整理）
* 記録した名言を自動的に通知して思い出させてくれる（習慣化）
<br>

## URL
URL:[GooglePlay 名言つんどく](https://play.google.com/store/apps/details?id=co.websarva.wings.android.myquotations)

![Google Play アプリ画像.png](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/599060/355530c4-789d-15c8-f74e-c946decf1c6d.png)

Qiitaにアプリ解説記事を投稿しています
[独学してAndroidアプリをリリースした話](https://qiita.com/Bu-Kohei/items/ad327cfeb6aba80c35cd)

<br>

## 開発環境と主なライブラリ

**開発環境**  
・Java:11.0.10  
・Android studio 4.1.3  
・CompileSdkVersion:30  
・MinSdkVersion:16  
・TargetSdkVersion:30  
・BuildToolsVersion:30.0.3  

**使用した主なライブラリ**  
・Room  
・ViewModel  
・LiveData  
・RecyclerView  
・Workmanager  
・AsyncTask  
・Notification  
・AlertDialog  
・Parcelable  
・Navigation  
<br>

## アプリの機能  

### タイトルごとの文章記録  

まず書籍のタイトルと著者名を入力して、書籍名のフォルダを作成します。  
<br>

![タイトル作成.gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/599060/61076807-7fa9-4220-e151-9497eee6f738.gif)  
<br>
<br>

作成した書籍のフォルダをタップして、その中に名言を作成します。 
<br>

![名言の作成.gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/599060/ce4ee007-57e7-3b25-1b31-9613caaa789d.gif)
<br>
<br>

これで書籍と名言の2つのRecyclerViewが出来上がります。書籍のRecyclerViewのHolderは、タイトル、著者名、作成日時を表示しており、Holderをタップすると、名言のRecyclerViewへ遷移します。 
<br>

![RecyclerView動作.gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/599060/4483761c-018f-d3e9-7bf1-bcd13d6e5c9e.gif)
<br>
<br>

## 文章をランダムに通知する  
この機能の動きは、「通知してほしい名言のCheckBoxをON ➜ CheckBoxがONになっているHolderからランダムに名言を取得 ➜ 定期的に通知される」というものです。各文章のCheckBoxをONすると通知されるようになります。
<br>

![チェックボックスON.gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/599060/1cd2dc89-6e9c-b470-b95e-a8bdc768b852.gif)
<br>
<br>

チェックをONにした文章がランダムに選択され、1行目がタイトル、2行目が文章の通知が届きます。  
文章の全文を確認する時は、届いた通知をタップするとアプリ内に飛んで全文が表示されます。
<br>

![通知タップ内容確認.gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/599060/5c1390c9-d21d-d623-2b87-0c770508eebc.gif)
<br>
<br>

通知履歴をタップしてもアプリに飛びます。
<br>

![通知履歴タップして内容確認.gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/599060/4b382c33-d4f5-c052-e521-eeea6743fec3.gif)
<br>
<br>

## フォルダーの並び替え
タイトル、著者名、作成日順で並び替え（昇順、降順）ができます。  
以下のgifはそれぞれ昇順から降順へ変更する様子です。
通知履歴をタップしてもアプリに飛びます。
<br>

### タイトル順  
![タイトル並び替え.gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/599060/b998dd19-8986-8b39-20ce-50c9334b2470.gif)
<br>
<br>

### 著者名順  
![著者名並び替え.gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/599060/554a612f-daee-0108-cc98-49911577cf18.gif)
<br>
<br>

### 作成日時順  
![作成日並び替え.gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/599060/6f10910d-b0df-ada0-3131-19e5a9372cfe.gif)
<br>
<br>

## 削除機能 
削除モードをONにすると、表示が赤くなり、この状態の時だけ右スワイプでの削除ができます。  
通知履歴をタップしてもアプリに飛びます。  
右スワイプでの削除はサクッと削除できるので取り入れましたが、一方で**操作ミスをすると間違って消してしまう危険がありました**。  
なので、削除モードを取り入れ、削除モードがONであることをはっきり示すために、画面の色を赤に変更する仕様にしました。
<br>

![ezgif.com-gif-maker (31) (1).gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/599060/d73f26db-8a66-3315-7c07-3e77f4245891.gif)
<br>
<br>

## 使い方の説明画面
アプリ作製の参考にするためにGoogle Playでメモ帳系のアプリをいろいろダウンロードしてみると、  
どう使えばいいのかイメージがつかみにくいものがいくつかありました。     
使った人がどう感じるかは意外と分からないものなのだと思うので、「使えば分かる」と思う部分についても一通り説明しています。
<br>

![使い方.gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/599060/cd492312-1098-a3bc-1813-fce0ee912aab.gif)
<br>
<br>

## その他の細かい機能
**タイトル重複の防止**  
気づかずに同じタイトルを複数作ってしまうことを避けるため、既存のタイトルと同じタイトルを作成しようとした場合、AlertDialogで作成不可のメッセージが出ます。
<br>

![アラートダイアローグ.gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/599060/50b45904-2cb6-0084-a051-a8d53016daee.gif)
<br>
<br>

**タイトル変更不可を知らせるのトースト**  
名言作成時にタイトル、著者名を変更すると、意図しない動作をしてしまうので、トーストでふわっと注意します。
<br>

![トースト.gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/599060/84c2ec9c-82e1-37e0-f3c2-a01e74d2877f.gif)
<br>
<br>

**吹き出しっぽいTextview**  
名言が表示されるTextview は吹き出しっぽくしました。この方が誰かが名言を言ってる感がちょっと増す気がするからです。  
Textview の下に、45度回転させたViewを配置しています。
<br>

![吹き出し.png](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/599060/398dc33c-6f84-59ec-7e12-c2ab547b2da3.png)
<br>
<br>

