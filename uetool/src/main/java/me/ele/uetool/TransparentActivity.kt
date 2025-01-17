package me.ele.uetool

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.appcompat.app.AppCompatActivity
import me.ele.uetool.base.DimenUtil

class TransparentActivity : AppCompatActivity() {
    private var vContainer: ViewGroup? = null
    private var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            finish()
            return
        }
        Util.setStatusBarColor(window, Color.TRANSPARENT)
        Util.enableFullscreen(window)
        setContentView(R.layout.uet_activity_transparent)

        vContainer = findViewById(R.id.container)

        val board = BoardTextView(this)
        board.setOnClickListener {
            UETool.getInstance().targetActivity.finish()
            finish()
        }

        type = intent.getIntExtra(EXTRA_TYPE, Type.TYPE_UNKNOWN)

        when (type) {
            Type.TYPE_EDIT_ATTR -> {
                val editAttrLayout = EditAttrLayout(this)
                editAttrLayout.setOnDragListener { offsetContent ->
                    board.updateInfo(
                        offsetContent
                    )
                }
                vContainer?.addView(editAttrLayout)
            }

            Type.TYPE_RELATIVE_POSITION -> vContainer?.addView(
                RelativePositionLayout(
                    this
                )
            )

            Type.TYPE_SHOW_GRIDDING -> {
                vContainer?.addView(GriddingLayout(this))
                board.updateInfo(
                    "LINE_INTERVAL: " + DimenUtil.px2dip(
                        GriddingLayout.LINE_INTERVAL.toFloat(), true
                    )
                )
            }

            else -> {
                Toast.makeText(this, getString(R.string.uet_coming_soon), Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.BOTTOM
        vContainer?.addView(board, params)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        UETool.getInstance().release()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    fun dismissAttrsDialog() {
        for (i in 0 until vContainer!!.childCount) {
            val child = vContainer!!.getChildAt(i)
            if (child is EditAttrLayout) {
                child.dismissAttrsDialog()
            }
        }
    }

    @IntDef(
        Type.TYPE_UNKNOWN, Type.TYPE_EDIT_ATTR, Type.TYPE_SHOW_GRIDDING, Type.TYPE_RELATIVE_POSITION
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class Type {
        companion object {
            const val TYPE_UNKNOWN: Int = -1
            const val TYPE_EDIT_ATTR: Int = 1
            const val TYPE_SHOW_GRIDDING: Int = 2
            const val TYPE_RELATIVE_POSITION: Int = 3
        }
    }

    companion object {
        const val EXTRA_TYPE: String = "extra_type"
    }
}
