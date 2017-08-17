package yuejunfeng.bawei.com.demo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by admin on 2017/8/16.
 * function:继承式控件
 1.继承listview,覆写构造方法
 2.覆写overScrollBy方法,重点关注deltaY,isTouchEvent方法
 3.暴露一个方法，去得到外界的ImageView,并测量ImageView控件的高度
 4.覆写OnToucheEvent方法
 */

public class OnBaseManager extends ListView {
    private ImageView listImage;
    private int drawableHeight;
    private int orignalHeight;

    public OnBaseManager(Context context) {
        this(context,null);
    }

    public OnBaseManager(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public OnBaseManager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
    public void setlistImage(ImageView listImage) {
        this.listImage = listImage;
        //获取图片的原始高度
        drawableHeight = listImage.getDrawable().getIntrinsicHeight();
        //获取ImageView控件的原始高度，以便回弹时，回弹到原始高度
        orignalHeight = listImage.getHeight();
    }


    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        System.out.println("A"+deltaY);
        System.out.println("B"+scrollY);
        System.out.println("C"+scrollRangeY);
        System.out.println("D"+isTouchEvent);
        //顶部下拉，用户触摸的操作才执行视差效果
        if(deltaY < 0 && isTouchEvent) {
            // deltaY是负值，我们要改为绝对值，累计给我们的iv_header高度
            int newHeight = listImage.getHeight() + Math.abs(deltaY);
            //把新的高度值赋值给控件，改变控件的高度
            if (newHeight <= drawableHeight) {
                listImage.getLayoutParams().height = newHeight;
                listImage.requestLayout();
            }

        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }


      @Override
          public boolean onTouchEvent(MotionEvent event) {
              switch (event.getAction()){
                  //松开
                  case MotionEvent.ACTION_UP:
                      //把当前的头布局的高度恢复初始高度
                      int height = listImage.getHeight();
                      //属性动画，改变高度的值，把我们当前头布局的高度，该为原始时的高度。
                      final ValueAnimator animator = ValueAnimator.ofInt(height, orignalHeight);
                     //动画更新的监听
                      animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                          private float animatedFraction;

                          @Override
                          public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            //获取动画执行过程中的分度值
                              animatedFraction = animator.getAnimatedFraction();
                              //获取中间的值，并赋给控件的新高度，可以使控件平稳回弹的效果
                              Integer animatedValue = (Integer) animator.getAnimatedValue();
                              //让新的高度值生效
                              listImage.getLayoutParams().height=animatedValue;
                              listImage.requestLayout();
                          }
                      });
                     // 动画的回弹效果，值越大，回弹越厉害
                      animator.setInterpolator(new OvershootInterpolator(2));
                      //设置动画设置的时间
                      animator.setDuration(1000);
                      //动画执行
                      animator.start();
                      break;
              }


              return super.onTouchEvent(event);
          }

}
