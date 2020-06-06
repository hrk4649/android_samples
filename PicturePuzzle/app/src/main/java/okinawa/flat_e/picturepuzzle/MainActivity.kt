package okinawa.flat_e.picturepuzzle

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Path
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
//import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.sqrt
import kotlin.random.Random

// アプリの状態
enum class MyAppStatus {
    /**
     * 開始画面
     */
    Start,
    /**
     * アニメーション中
     */
    InAnimation,
    /**
     * ゲーム中
     */
    OnGame
}

class Board {
    var numOfPiece:Int = -1
    var pieceWidth:Int = -1
    var pieceHeight:Int = -1

    // ボード は配列で表現される。16ピースのボードの場合、
    // インデックス番号 n の要素は、
    // ボードの(x, y) = (n % 4, n / 4)のピースに対応する
    var pieces : Array<Int> = Array(0){0}

    // 掴んている画像の番号
    var numGrabbed:Int = -1

    // ピースを表示する際のx座標
    private var originX:Int = 0
    // ピースを表示する際のy座標
    private var originY:Int = 0

    // ピース間のマージン
    private val marginPiece:Int = 10

    // 表示するビットマップのデバイス画面サイズに対する比率
    private val tgtPixelRate = 0.9

    /**
     * 表示用ビットマップの初期化
     *
     * 座標関連の変数の初期化も合わせて行う
     *
     * @param dMetrics デバイスの画面サイズ
     * @param bitmap0 オリジナルのビットマップ
     * @param numOfPiece ピース数
     * @return ディスプレイに表示する縮小版のビットマップ
     */
    fun initBitmap1(dMetrics:DisplayMetrics, bitmap0:Bitmap, numOfPiece:Int): Bitmap {
        this.numOfPiece = numOfPiece
        // ビットマップをデバイスの画面サイズに合わせて縮小・拡大する
        // ビットマップとデバイス画面の幅、高さの比率を求める
        val rateWidth = bitmap0.width.toFloat() / dMetrics.widthPixels.toFloat()
        val rateHeight = bitmap0.height.toFloat() / dMetrics.heightPixels.toFloat()

        // オリジナル画像のサイズ(比率の大きい辺を基準にする)
        val orgPxlSize = if (rateWidth > rateHeight) {
            // 幅の比率が大きい
            bitmap0.width.toFloat()
        } else {
            // 高さの比率が大きい
            bitmap0.height.toFloat()
        }
        // 目標のサイズ = 画面サイズ *
        val tgtPxlSize = if (rateWidth > rateHeight) {
            (dMetrics.widthPixels * tgtPixelRate).toFloat()
        } else {
            (dMetrics.heightPixels * tgtPixelRate).toFloat()
        }

        // https://developer.android.com/reference/kotlin/android/graphics/Matrix#prescale_1
        val matrix = Matrix()
        matrix.preScale(tgtPxlSize / orgPxlSize, tgtPxlSize / orgPxlSize)

        // 縮小・拡大されたビットマップ
        val bitmap1 = Bitmap.createBitmap(bitmap0, 0, 0,
            bitmap0.width, bitmap0.height, matrix, true)

        // 縦、または、横で並ぶピースの数
        val numPieceRowClm = sqrt(numOfPiece.toDouble()).toInt()

        // 分割されたビットマップのサイズ
        pieceWidth = bitmap1.width / numPieceRowClm
        pieceHeight = bitmap1.height / numPieceRowClm

        // ボードのサイズ
        val boardWidth = pieceWidth * numPieceRowClm + marginPiece * (numPieceRowClm - 1)
        val boardHeight = pieceHeight * numPieceRowClm + marginPiece * (numPieceRowClm - 1)

        // ピースを置く際の原点
        originX = 0 + dMetrics.widthPixels / 2 - boardWidth / 2
        originY = 0 + dMetrics.heightPixels / 2  - boardHeight / 2

        return bitmap1
    }


    // ボード上のピースを初期化する
    fun initPieces() {
        pieces = Array(numOfPiece){0}
        for ( n in 0 until numOfPiece) {
            pieces[n] = n
        }
    }

    // ピースが正しい位置にセットされたか?
    fun isAllPiecesOnProperPlaces():Boolean {
        // pieces[n] = n であれば正しい位置に置かれている。
        return pieces.filterIndexed {index, piece -> index == piece}.size == pieces.size
    }

    // シャッフルしたピースの配列を返す
    fun getShuffledPieces(): Array<Int> {
        // ピースをバラバラにした状態のボード
        val shuffled = pieces.clone()
        // Fisher–Yates shuffle
        // https://blog.y-yuki.net/entry/2018/08/22/094000
        for (idx1 in numOfPiece - 1 downTo 0) {
            val idx2 = Random.nextInt(0, idx1 + 1)
            val value1 = shuffled[idx1]
            val value2 = shuffled[idx2]
            shuffled[idx1] = value2
            shuffled[idx2] = value1
        }
        return shuffled
    }

    // ボード上の列番号を返す
    private fun getBoardClm(n:Int):Int {
        return n % sqrt(this.numOfPiece.toDouble()).toInt()
    }
    // ボード上の行番号を返す
    private fun getBoardRow(n:Int):Int {
        return n / sqrt(this.numOfPiece.toDouble()).toInt()
    }

    // 縮小・拡大されたビットマップ上のX座標を返す
    fun getOrigBitmapX(n:Int):Int {
        return getBoardClm(n) * pieceWidth
    }
    // 縮小・拡大されたビットマップ上のX座標を返す
    fun getOrigBitmapY(n:Int):Int {
        return getBoardRow(n) * pieceHeight
    }


    // ディスプレイ上のX座標を返す
    fun getDisplayX(n:Int):Float {
        return (originX + getBoardClm(n) * pieceWidth + marginPiece * (getBoardClm(n) - 1)).toFloat()
    }
    // ディスプレイ上のY座標を返す
    fun getDisplayY(n:Int):Float {
        return (originY + getBoardRow(n) * pieceHeight + marginPiece * (getBoardRow(n) - 1)).toFloat()
    }
    // ディスプレイ上の ボートの ピース番号を返す
    fun getDisplayN(x:Float, y:Float):Int {
        var ret = -1
        for (n in 0 until numOfPiece) {
            val dx = getDisplayX(n)
            val dy = getDisplayY(n)
            if (x >= dx && x < dx + pieceWidth
                && y >= dy && y < dy + pieceHeight) {
                ret = n
                break
            }
        }
        return ret
    }
}

class MainActivity : AppCompatActivity() {
    // 画面をタッチしてつかんだイメージビュー
    private var grabbedIV : ImageView? = null


    // イメージビューをつかんだ時の、イメージビュー座標系でのタッチされた位置
    private var grabbedX :Float = -1.0f
    private var grabbedY :Float = -1.0f


    // 分割されたビットマップのイメージビューのリスト
    private var ivList = mutableListOf<ImageView>()

    // ボードを構成するピースの数
    private var numOfPiece = 4 * 4

    // 分割されたビットマップを表示する論理上のボード
    var board = Board()

    // アプリの状態
    var myAppStatus = MyAppStatus.Start

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        initImageViews()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initImageViews() {
        // 画面サイズ(application display area)の取得
        // https://akira-watson.com/android/screen-size.html
        // onWindowFocusChanged呼び出し時にはレイアウトのサイズが判明する
        val layout = findViewById<ConstraintLayout>(R.id.mainLayout)
        val dMetrics = DisplayMetrics()
        dMetrics.heightPixels = layout.height
        dMetrics.widthPixels = layout.width

        // 1枚の画像を分割して表示する
        // (参考)リソース画像から加工した画像を作る
        // https://akira-watson.com/android/matrix.html
        val bitmap0 = BitmapFactory.decodeResource(
                resources,
                R.drawable.leaf_small
        )

        // 表示用ビットマップを用意する
        val bitmap1 = board.initBitmap1(dMetrics, bitmap0, numOfPiece)

        // リストの初期化
        ivList = mutableListOf()

        // 分割した画像を作成する
        for (n in 0 until numOfPiece) {
            // ビットマップ画像の切り出し
            val bitmap = Bitmap.createBitmap(bitmap1,
                    board.getOrigBitmapX(n),
                    board.getOrigBitmapY(n),
                    board.pieceWidth,
                    board.pieceHeight,
                    null,
                    true)

            // 画像を表示するためのイメージビューの作成
            // TODO 表示機能の切り出し
            val imageView = ImageView(this).apply {
                setImageBitmap(bitmap)
                // 画像の表示位置を変更する
                x = board.getDisplayX(n)
                y = board.getDisplayY(n)
            }
            ivList.add(imageView)
        }

        // 既存のレイアウトにイメージビューを追加する
        val mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
        // すでにイメージビューがある場合は、レイアウトから削除しておく
        //mainLayout.removeAllViews()

        mainLayout.apply {
            for (i in ivList) {
                addView(i)
            }
        }

        // タッチイベントの設定
        mainLayout.setOnTouchListener{
            v:View?, event: MotionEvent? ->
            this.onTouch(v,event)
        }
    }

    /**
     * タッチイベント
     */
    private fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Log.d("onTouch", "v $v event $event")
        // warningが出たため対応する
        v?.performClick()

        when(myAppStatus) {
            MyAppStatus.Start -> onTouchStart(v, event)
            MyAppStatus.OnGame -> onTouchOnGame(v, event)
            else -> {}
        }
        // イベントを伝搬させない
        return true
    }

    /**
     * タッチイベント Start 時
     */
    private fun onTouchStart(v: View?, event: MotionEvent?): Boolean {
        Log.d("onTouchStart", "v $v event $event")
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                // ゲームの初期化
                board.initPieces()

                // ゲームクリア時のメッセージを隠しておく
                val textViewGameClear = findViewById<TextView>(R.id.textViewGameClear)
                textViewGameClear.visibility = TextView.INVISIBLE

                // ピースを初期位置にセットする
                for(n in 0 until board.numOfPiece) {
                    val ivIdx = board.pieces[n]
                    ivList[ivIdx].apply {
                        x = board.getDisplayX(n)
                        y = board.getDisplayY(n)
                    }
                }

                // ピースをバラバラにした状態のボード
                val shuffled  = board.getShuffledPieces()

                // ピースをバラバラの状態で表示する


                // アニメーションを同時実行するためのダミー
                val dummyAnime = ObjectAnimator.ofInt(0,0)
                val animeSet = AnimatorSet()
                for(n in 0 until board.numOfPiece) {
                    val ivIdx = shuffled[n]
                    val i = ivList[ivIdx]
                    val destX = board.getDisplayX(n)
                    val destY = board.getDisplayY(n)
                    val obj = ObjectAnimator.ofFloat(i, View.X, View.Y, Path().apply {
                        //lineTo(centerX, centerY)
                        moveTo(i.x, i.y)
                        lineTo(destX, destY)
                    })
                    animeSet.play(dummyAnime).with(obj)
                }
                animeSet.apply {
                    duration = 1000
                    addListener(object: AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            // アニメーション終了後にステータスをゲーム中に変更する
                            myAppStatus = MyAppStatus.OnGame
                        }
                    })
                    start()
                }

                board.pieces = shuffled

                // アニメーション中にする
                myAppStatus = MyAppStatus.InAnimation
            }
            else -> {
                // 何もしない
            }
        }
        return true
    }

    /**
     * タッチイベント OnGame 時
     */
    private fun onTouchOnGame(v: View?, event: MotionEvent?): Boolean {
        // イベントが発生した座標
        // event.x, event.y と event.rawX, event.rawYでは原点の位置が異なる
        // https://stackoverflow.com/questions/20636163/difference-between-motionevent-getrawx-and-motionevent-getx
        val x = event?.x ?: - 1.0f
        val y = event?.y ?: - 1.0f
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                // つかんだ画像の移動
                if (grabbedIV != null) {
                    Log.d("onTouch", "v $v event $event")
                    Log.d("onTouch", "MOVE")
                    grabbedIV?.x = x - grabbedX
                    grabbedIV?.y = y - grabbedY
                }
            }
            MotionEvent.ACTION_DOWN -> {
                // 分割された画像をつかむ

                // 該当するピース
                val pieceN = board.getDisplayN(x, y)
                if (pieceN >= 0 ) {
                    board.numGrabbed = pieceN
                    grabbedIV = ivList[board.pieces[pieceN]]
                    // イメージビューのどの位置をつかんだのか覚えておく
                    grabbedX = x - grabbedIV!!.x
                    grabbedY = y - grabbedIV!!.y

                }
                // つかんだ画像を最上位にする
                // change order of views in linear layout android
                // https://stackoverflow.com/questions/17776306/change-order-of-views-in-linear-layout-android
                if (grabbedIV != null) {
                    // var mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
                    val mainLayout = v as ConstraintLayout
                    mainLayout.removeView(grabbedIV)
                    mainLayout.addView(grabbedIV)
                }
            }
            MotionEvent.ACTION_UP -> {
                Log.d("onTouch", "v $v event $event")
                Log.d("onTouch", "RELEASE")
                // 画像を離す処理： 他の画像の上で離す場合は、その画像と入れ替える
                // それ以外の場合は離した画像を元の位置に戻す
                val numSwitched = board.getDisplayN(x, y)
                if (numSwitched >= 0 ) {
                    // 画像の入れ替えアニメーション
                    val switchedIV = ivList[board.pieces[numSwitched]]

                    val dummyAnime = ObjectAnimator.ofInt(0,0)
                    val animeSet = AnimatorSet()

                    // ピース1
                    val obj1 = ObjectAnimator.ofFloat(
                        switchedIV, View.X, View.Y, Path().apply {
                        moveTo(switchedIV.x, switchedIV.y)
                        lineTo(
                            board.getDisplayX(board.numGrabbed),
                            board.getDisplayY(board.numGrabbed))
                    })
                    animeSet.play(dummyAnime).with(obj1)

                    // ピース2
                    val obj2 = ObjectAnimator.ofFloat(
                        grabbedIV, View.X, View.Y, Path().apply {
                            moveTo(grabbedIV!!.x, grabbedIV!!.y)
                            lineTo(
                                board.getDisplayX(numSwitched),
                                board.getDisplayY(numSwitched))
                        })
                    animeSet.play(dummyAnime).with(obj2)

                    animeSet.apply {
                        duration = 300
                        addListener(object: AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                super.onAnimationEnd(animation)
                                // アニメーション終了後にゲームクリア判定をする
                                if (board.isAllPiecesOnProperPlaces()) {
                                    // ゲームクリアの処理へ
                                    // ゲームクリア時のメッセージを表示する
                                    val textViewGameClear = findViewById<TextView>(R.id.textViewGameClear)
                                    textViewGameClear.visibility = TextView.VISIBLE

                                    // レイアウトの最上位に表示する
                                    val mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
                                    mainLayout.removeView(textViewGameClear)
                                    mainLayout.addView(textViewGameClear)

                                    // ステータスは開始画面にする
                                    myAppStatus = MyAppStatus.Start
                                } else {
                                    // ステータスをゲーム中に変更する
                                    myAppStatus = MyAppStatus.OnGame
                                }
                            }
                        })
                        start()
                    }

                    // ピースの位置を入れ替えする
                    val vGrabbed = board.pieces[board.numGrabbed]
                    val vSwitched = board.pieces[numSwitched]
                    board.pieces[board.numGrabbed] = vSwitched
                    board.pieces[numSwitched] = vGrabbed

                } else {
                    // 画像を元に戻す
                    if (grabbedIV != null) {
                        grabbedIV!!.x = board.getDisplayX(board.numGrabbed)
                        grabbedIV!!.y = board.getDisplayY(board.numGrabbed)
                    }
                }
                board.numGrabbed = -1
                grabbedIV = null
            }
            else -> {
                // 何もしない
            }
        }

        return true
    }
}

