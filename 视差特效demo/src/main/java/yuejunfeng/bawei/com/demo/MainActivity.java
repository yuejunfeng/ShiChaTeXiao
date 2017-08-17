package yuejunfeng.bawei.com.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private OnBaseManager onlist_view;
    private ImageView iv_image;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //找到控件资源
        onlist_view = (OnBaseManager) findViewById(R.id.onlist_view);
        //ListView添加一个头布局
        header =  View.inflate(this, R.layout.header, null);
        iv_image = (ImageView) header.findViewById(R.id.iv_image);
        //等View界面全部绘制完毕的时候，去得到已经绘制完控件的宽和高，查一下这个方法，并做一个笔记
        iv_image.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //宽跟高已经测量完毕
                onlist_view.setlistImage(iv_image);
                //释放资源
                iv_image.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        //使用ListView的ArrayAdapter适配器
        onlist_view.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Chinese.NAMES));
        onlist_view.addHeaderView(header);
    }
}
