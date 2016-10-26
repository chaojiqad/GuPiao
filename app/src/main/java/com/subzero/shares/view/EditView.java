package com.subzero.shares.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

public class EditView extends ImageView {

  private int maxWidth, maxHeight, minWidth, minHeight;// 极限值

  private int img_H, img_W, img_X, img_Y;//实时宽高和位置信息

  private int current_Top, current_Right, current_Bottom, current_Left;// 当前图片上下左右坐标

  private int start_x, start_y, current_left, current_top;// 触摸位置
  
  private int maxLeft,maxRight,maxTop,maxBottom;

  private float beforeLenght, beforeLenght_X, beforeLenght_Y, afterLenght,
      afterLenght_X, afterLenght_Y;// 两触点距离

  private float scale_temp;// 缩放比例

  private int mode = 0;// 操作标志：0-无操作 1-拖拽 20-等比例缩放 21-水平缩放 22-竖直缩放 
  
  private int firstX,firstY,secondX,secondY,leftInc=0,rightInc=0,topInc=0,bottomInc=0;

  /** 构造方法 **/
  public EditView(Context context) {
    super(context);
  }
  
  public void  setmaxLocation(int [] maxlocation){
    this.maxLeft=maxlocation[0];
    this.maxRight=maxlocation[1];
    this.maxTop=maxlocation[2];
    this.maxBottom=maxlocation[3];
    maxWidth = this.maxRight-this.maxLeft;
    maxHeight = this.maxBottom-this.maxTop;

    minWidth = maxWidth/30;
    minHeight = maxHeight / 30;
  }

  /** 设定图片宽度 **/
  public void setimg_W(int img_W) {
    this.img_W = img_W;
  }
  
  /** 设定图片高度 **/
  public void setimg_H(int img_H) {
    this.img_H = img_H;
  }
  
  /** 获取图片宽度 **/
  public int getimg_W() {
    return img_W;
  }

  /** 获取图片高度 **/
  public int getimg_H() {
    return img_H;
  }

  /** 获取图片横坐标 **/
  public int getimg_X() {
    return img_X;
  }

  /** 获取图片纵坐标 **/
  public int getimg_Y() {
    return img_Y;
  }

  
  /** 获取图片的操作状态 **/
  public int getMode(){
    return this.mode;
  }

  public EditView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right,
      int bottom) {
    super.onLayout(changed, left, top, right, bottom);
  }// 必须继承此方法否则就会造成缩放的时候图片消失！

  /***
   * touch 事件
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    /** 处理单点、多点触摸 **/
      switch (event.getAction() & MotionEvent.ACTION_MASK) {//多点触摸，获取下一个动作
      case MotionEvent.ACTION_DOWN:
        firstX=(int) event.getX();
        firstY=(int) event.getY();
        onTouchDown(event);
        postInvalidate();
        break;
      case MotionEvent.ACTION_POINTER_DOWN:// 另一个触摸点按下
        secondX=(int) event.getX(1);
        secondY=(int) event.getY(1);
        if((secondX<=this.getimg_W())&&(secondX>=0)&&(secondY>=0)&&(secondY<=this.getimg_H())){
          onPointerDown(event);
        }
        break;

      case MotionEvent.ACTION_MOVE:
        onTouchMove(event);
        break;
      case MotionEvent.ACTION_UP:
        mode = 0;
        break;
      case MotionEvent.ACTION_POINTER_UP:
        mode = 0;
        break;
      }

    return true;
  }

  /** 按下 **/
  void onTouchDown(MotionEvent event) {
    mode = 1;

    current_left = this.getLeft();
    current_top = this.getTop();// 获取的是相对于屏幕左上角的坐标

    start_x = (int) event.getRawX();
    start_y = (int) event.getRawY();// 获取是相对于控件左上角的坐标
    img_X = this.getLeft();
    img_Y = this.getTop();
  }

  /** 两个手指 只能放大缩小 **/
  void onPointerDown(MotionEvent event) {
    if (event.getPointerCount() == 2) {
      int x_long = (int) Math.abs(event.getX(0) - event.getX(1));
      int y_long = (int) Math.abs(event.getY(0) - event.getY(1));
      if (y_long == 0)
        y_long = 1;//分母不得为零
      double xdy = x_long / y_long;
      if (xdy >= 2f){
        int smfX=(int) (event.getX(1)-event.getX(0));
        leftInc=(smfX>0)?(0):(1);
        rightInc=(smfX>0)?(1):(0);
        mode = 21;
        }
      else if (xdy <= 0.5f){
        int smfY=(int) (event.getY(1)-event.getY(0));
        topInc=(smfY>0)?(0):(1);
        bottomInc=(smfY>0)?(1):(0);
        mode = 22;
        }
      else{
        int smfX=(int) (event.getX(1)-event.getX(0));
        leftInc=(smfX>0)?(0):(1);
        rightInc=(smfX>0)?(1):(0);
        int smfY=(int) (event.getY(1)-event.getY(0));
        topInc=(smfY>0)?(0):(1);
        bottomInc=(smfY>0)?(1):(0);
        mode = 20;/*根据x/y的竖直判断缩放类型*/
      }
      beforeLenght = getDistance(event);// 获取两点的距离
      beforeLenght_X = getDistance_X(event);// 获取两点的X距离
      beforeLenght_Y = getDistance_Y(event);// 获取两点的Y距离
    }
  }
  
  /** 获取两点的距离 **/
  float getDistance(MotionEvent event) {
    float x = firstX - event.getX(1);
    float y = firstY - event.getY(1);
    return FloatMath.sqrt(x * x + y * y);
  }

  float getDistance_X(MotionEvent event) {
    float x = firstX - event.getX(1);
    return Math.abs(x);
  }

  float getDistance_Y(MotionEvent event) {
    float y = firstY - event.getY(1);
    return Math.abs(y);
  }

  /** 移动的处理 **/
  void onTouchMove(MotionEvent event) {
    int left = 0, top = 0, right = 0, bottom = 0;
    /** 处理拖动 **/
    if (mode == 1) {
      /** 获取相应的l，t,r ,b **/
      left=current_left+(int) event.getRawX()-start_x;
      right=current_left+(int) event.getRawX()-start_x+img_W;
      top=current_top+(int) event.getRawY()-start_y;
      bottom=current_top+(int) event.getRawY()-start_y+img_H;
      start_x=(int) event.getRawX();
      start_y=(int) event.getRawY();
      current_left=left;
      current_top=top;
      img_H=this.getHeight();
      img_W=this.getWidth();
      /** 在这里要进行判断处理，防止在drag时候越界 **/
      if (left <= maxLeft) {
        left = maxLeft;
        right = this.getWidth();
      }
      if (right >= maxRight) {
        left = maxRight - this.getWidth();
        right = maxRight;
      }
      if (top <= maxTop) {
        top = maxTop;
        bottom =maxTop + img_H;
      }
      if (bottom >= maxBottom) {                                   
        top = maxBottom - img_H;
        bottom = maxBottom;
      }
      
      img_X = left;
      img_Y = top;
      this.setPosition(left, top, right, bottom);//更新位置

    }
    /** 等比例缩放 **/
    else if (mode == 20) {
      afterLenght = getDistance(event);
      float gapLenght = afterLenght - beforeLenght;// 变化的长度
      if (Math.abs(gapLenght) > 5f) {
        scale_temp = afterLenght / beforeLenght;// 求的缩放的比例
        this.setScale(scale_temp);
        beforeLenght = afterLenght;
      }
      /** 水平缩放 **/
    } else if (mode == 21) {
      afterLenght_X = getDistance_X(event);
      float gapLenght_X = afterLenght_X - beforeLenght_X;// 变化的长度
      if (Math.abs(gapLenght_X) > 3f) {
        scale_temp = afterLenght_X / beforeLenght_X;// 求的缩放的比例
        this.setScale(scale_temp);
        beforeLenght_X = afterLenght_X;
      }
      /** 豎直缩放 **/
    } else if (mode == 22) {
      afterLenght_Y = getDistance_Y(event);
      float gapLenght_Y = afterLenght_Y - beforeLenght_Y;// 变化的长度
      if (Math.abs(gapLenght_Y) > 3f) {
        scale_temp = afterLenght_Y / beforeLenght_Y;// 求的缩放的比例
        this.setScale(scale_temp);
        beforeLenght_Y = afterLenght_Y;
      }
    }

  }

  /** 实现处理拖动 **/
  private void setPosition(int left, int top, int right, int bottom) {
    this.layout(left, top, right, bottom);
  }

  /** 处理缩放 **/
  void setScale(float scale) {
    int disX = (int) (this.getWidth() * Math.abs(1 - scale)) / 4;// 获取缩放水平距离
    int disY = (int) (this.getHeight() * Math.abs(1 - scale)) / 4;// 获取缩放垂直距离

    // 放大
    if (scale > 1 && this.getWidth() <= (maxWidth) && this.getHeight() <= (maxHeight)) {
      if (20 == mode) {
        if (current_Left > maxLeft) {
          current_Left = this.getLeft() - disX*leftInc;
        }else{
          current_Left = this.getLeft();
        }
        
        firstX+=disX*leftInc;
        if (current_Top > maxTop) {
          current_Top = this.getTop() - disY*topInc;
        }else{
          current_Top = this.getTop();
        }
        
        firstY+=disY*topInc;
        if (current_Right < maxRight) {
          current_Right = this.getRight() + disX*rightInc;
        }else{
          current_Right = this.getRight();
        }
        
        if (current_Bottom < maxBottom) {
          current_Bottom = this.getBottom() + disY*bottomInc;
        }else{
          current_Bottom = this.getBottom();
        }
        
      } else if (22 == mode) {
        current_Left = this.getLeft();
        if (current_Top > maxTop) {
          current_Top = this.getTop() - disY*topInc;
        }else{
          current_Top = this.getTop();
        }
        firstY+=disY*topInc;
        current_Right = this.getRight();
        if (current_Bottom < maxBottom) {
          current_Bottom = this.getBottom() + disY*bottomInc;
        }else{
          current_Bottom = this.getBottom();
        }
        
      } else if (21 == mode) {
        if (current_Left > maxLeft) {
          current_Left = this.getLeft() - disX*leftInc;
        }else{
          current_Left = this.getLeft();
        }
        firstX+=disX*leftInc;
        current_Top = this.getTop();
        if (current_Right < maxRight) {
          current_Right = this.getRight() + disX*rightInc;
        }else{
          current_Right = this.getRight();
        }
        current_Bottom = this.getBottom();
      }
      img_W = current_Right - current_Left;
      img_H = current_Bottom - current_Top;
    /*	if (img_W >= layout_W) {
        current_Left = maxLeft;//////////////////////////////////////////////////////////////
        current_Right = maxRight;/////////////////////////////////////////////////////////
      }
      if (img_H >= screen_H) {
        current_Top = maxTop;////////////////////////////////////////////////////
        current_Bottom = maxBottom;//////////////////////////////////////big
      }*/
      if (current_Left <= maxLeft) {
        current_Left = maxLeft;
        //current_Right = img_W;
      }
      if (current_Right >= maxRight) {//////////////////////////////////////////////////////////////bigright
        //current_Left = layout_W - img_W;
        current_Right = maxRight;
      }
      if (current_Top <= maxTop) {
        current_Top = maxTop;
        //current_Bottom = img_H;
      }
      if (current_Bottom >= maxBottom) {
        //current_Top = screen_H - img_H;
        current_Bottom = maxBottom;
      }// ////////////防止放大控件时，图片出边界
      img_X = current_Left;
      img_Y = current_Top;
      img_W = current_Right - current_Left;
      img_H = current_Bottom - current_Top;
      this.setFrame(current_Left, current_Top, current_Right,
          current_Bottom);
      postInvalidate();
      /***
       * 此时因为考虑到对称，所以只做一遍判断就可以了。
       */
    }
    
    // 缩小
    else if (scale < 1 && this.getWidth() >= minWidth && this.getHeight() >= minHeight) {
      if (20 == mode) {
        current_Left = this.getLeft() + disX*leftInc;
        firstX-=disX*leftInc;
        current_Top = this.getTop() + disY*topInc;
        firstY-=disY*topInc;
        current_Right = this.getRight() - disX*rightInc;
        current_Bottom = this.getBottom() - disY*bottomInc;
      } else if (22 == mode) {
        current_Left = this.getLeft();
        current_Top = this.getTop() + disY*topInc;
        firstY-=disY*topInc;
        current_Right = this.getRight();
        current_Bottom = this.getBottom() - disY*bottomInc;
      } else if (21 == mode) {
        current_Left = this.getLeft() + disX*leftInc;
        firstX-=disX*leftInc;
        current_Top = this.getTop();
        current_Right = this.getRight() - disX*rightInc;
        current_Bottom = this.getBottom();
      }
      img_X = current_Left;
      img_Y = current_Top;
      img_W = current_Right - current_Left;
      img_H = current_Bottom - current_Top;
      this.setFrame(current_Left, current_Top, current_Right,
          current_Bottom);
      postInvalidate();//更新紅色邊框
    }
  }
}