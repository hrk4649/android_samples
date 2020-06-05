package okinawa.flat_e.samplefallapart

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.max
import kotlin.math.sqrt


class MainActivity : AppCompatActivity(),View.OnTouchListener {

    // 画面をタッチしてつかんだイメージビュー
    private var grabbedIV : ImageView? = null


    // イメージビューをつかんだ時の、イメージビュー座標系でのタッチされた位置
    private var grabbedX :Float = -1.0f
    private var grabbedY :Float = -1.0f


    // 分割されたビットマップのイメージビューのリスト
    private var ivList = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //onCreate1()
        onCreate2()
    }

    fun onCreate2() {
        // 画面サイズ(application display area)の取得
        // https://akira-watson.com/android/screen-size.html
        var dMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dMetrics)


        // 1枚の画像を分割して表示する

        // (参考)リソース画像から加工した画像を作る
        // https://akira-watson.com/android/matrix.html
        val bitmap0 = BitmapFactory.decodeResource(
            resources,
            R.drawable.leaf_small
        )

        // 画像を画面サイズに合わせて縮小・拡大する
        // オリジナル画像のサイズ(幅、高さの長いほう)
        val orgSize = max(bitmap0.width, bitmap0.height).toFloat()
        // 目標のサイズ = 画面サイズ(スマフォをタテにした場合の幅) * 0.9
        val tgtSize = (dMetrics.widthPixels * 0.9).toFloat()

        // https://developer.android.com/reference/kotlin/android/graphics/Matrix#prescale_1
        var matrix = Matrix()
        matrix.preScale(tgtSize / orgSize, tgtSize.toFloat() / orgSize)

        val bitmap1 = Bitmap.createBitmap(bitmap0, 0, 0,
            bitmap0.width, bitmap0.height, matrix, true);

        // リストの初期化

        ivList = mutableListOf<ImageView>()

        // 分割数
        var numOfPiece = 4 * 4
        //var numOfPiece = 1

        // 分割された画像のサイズ
        val pieceWidth = (bitmap1.width / sqrt(numOfPiece.toDouble())).toInt()
        val pieceHeight = (bitmap1.height / sqrt(numOfPiece.toDouble())).toInt()

        // 分割した画像を作成する
        for (n in 0 until numOfPiece) {
            val x = n %  sqrt(numOfPiece.toDouble()).toInt()
            val y = n /  sqrt(numOfPiece.toDouble()).toInt()

            // ビットマップ画像の切り出し
            val bitmap = Bitmap.createBitmap(bitmap1, x * pieceWidth, y * pieceHeight,
                pieceWidth, pieceHeight, null, true);

            // 画像を表示するためのイメージビューの作成
            val imageView = ImageView(this).apply {
                setImageBitmap(bitmap)
                // 画像の表示位置を変更する
                setX((x * pieceWidth * 1.01).toFloat())
                setY((y * pieceHeight* 1.01).toFloat())
            }
            ivList.add(imageView)
        }

        // 既存のレイアウトにイメージビューを追加する
        var mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
        // すでにイメージビューがある場合は、レイアウトから削除しておく
        mainLayout.removeAllViews()

        mainLayout.apply {
            for (i in ivList) {
                addView(i)
            }
        }

        // タッチイベントの設定
        mainLayout.setOnTouchListener(this)
//        for (i in ivList) {
//            i.setOnTouchListener { v, event ->
//                Log.d("onCreate2", "v $v event $event")
//                true
//            }
//        }
    }

    fun onCreate1() {
        // 画像を表示する
        // https://developer.android.com/guide/topics/graphics/drawables
        // Instantiate an ImageView and define its properties
        val i = ImageView(this).apply {
            setImageResource(R.drawable.leaf_small)
            contentDescription = resources.getString(R.string.app_name)

            // set the ImageView bounds to match the Drawable's dimensions
            //adjustViewBounds = true

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        // 既存のレイアウトにイメージビューを追加する
        var mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
        mainLayout.apply {
            addView(i)
        }
    }

    /**
     * タッチイベント
     */
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        //Log.d("onTouch", "v $v event $event")
        // イベントが発生した座標
        // event.x, event.y と event.rawX, event.rawYでは原点の位置が異なる
        // https://stackoverflow.com/questions/20636163/difference-between-motionevent-getrawx-and-motionevent-getx
        // val x = event?.rawX ?: - 1.0f
        // val y = event?.rawY ?: - 1.0f
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
                for (i in ivList) {
                    // この画像の範囲に、イベントが発生した座標をふくむか?
                    if (i.x <= x
                        && i.x + i.width > x
                        && i.y <= y
                        && i.y + i.height > y
                    ) {
                        Log.d("onTouch", "v $v event $event")
                        Log.d("onTouch", "GRAB x = $x y = $y i.x = ${i.x} i.y = ${i.y} i.width = ${i.width} i.height = ${i.height} ")
                        grabbedIV = i
                        // イメージビューのどの位置をつかんだのか覚えておく
                        grabbedX = x - i.x
                        grabbedY = y - i.y
                        break
                    }
                }
                // つかんだ画像を最上位にする
                // change order of views in linear layout android
                // https://stackoverflow.com/questions/17776306/change-order-of-views-in-linear-layout-android
                if (grabbedIV != null) {
                    // var mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
                    var mainLayout = v as ConstraintLayout
                    mainLayout.removeView(grabbedIV)
                    mainLayout.addView(grabbedIV)
                }
            }
            MotionEvent.ACTION_UP -> {
                // 分割された画像をはなす
                Log.d("onTouch", "v $v event $event")
                Log.d("onTouch", "RELEASE")
                grabbedIV = null
            }
            else -> {
                // 何もしない
            }
        }

        // イベントを伝搬させない
        return true;
    }
}


