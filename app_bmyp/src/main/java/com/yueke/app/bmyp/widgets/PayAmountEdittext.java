package com.yueke.app.bmyp.widgets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import com.askia.keyboard.widget.VirtualKeyboardView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

@SuppressLint("AppCompatCustomView")
public class PayAmountEdittext extends EditText {

    private Context context;
    private Button button;
    private KeyboardDialog mKeyboard;
    private InputAccountListener inputAccountListener;
    private boolean isHideDecimal = false;

    public PayAmountEdittext(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public PayAmountEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public PayAmountEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void setInputAccountListener(InputAccountListener inputAccountListener) {
        this.inputAccountListener = inputAccountListener;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public void hideDecimal() {
        isHideDecimal = true;
    }

    private void init() {
        hideSoftInpuMethod();
    }

    private void hideSoftInpuMethod() {
        // 设置不调用系统键盘
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            this.setInputType(InputType.TYPE_NULL);
        } else {
            ((Activity) this.context).getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(this, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {

        if (TextUtils.isEmpty(text)) {
            if (inputAccountListener != null) {
                inputAccountListener.inputAccount(0f);
            }
            if (button != null) {
                button.setEnabled(false);
            }
            return;
        }
        //以小数点开始或者结尾
        if (text.toString().endsWith(".") || text.toString().startsWith(".")) {
            if (inputAccountListener != null) {
                inputAccountListener.inputAccount(0f);
            }
            if (button != null) {
                button.setEnabled(false);
            }
            return;
        }

        //金额小于0
        if (Float.valueOf(text.toString()) < 0) {
            if (inputAccountListener != null) {
                inputAccountListener.inputAccount(0f);
            }
            if (button != null) {
                button.setEnabled(false);
            }
            return;
        }
        if (inputAccountListener != null) {
            inputAccountListener.inputAccount(Float.valueOf(text.toString()));
        }
        if (button != null) {
            button.setEnabled(true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        ViewParent viewParent = this.getParent();
        while (viewParent != null) {
            if (viewParent instanceof ScrollView) {
                ((ScrollView) viewParent).scrollTo(0, 1000);
                ((ScrollView) viewParent).setOnDragListener(scrollListener);
                break;
            }
            viewParent = viewParent.getParent();
        }
        if (event.getAction() != 0) {
            return true;
        } else {
            if (this.mKeyboard == null) {
                this.mKeyboard = new KeyboardDialog(this.context, this, isHideDecimal);
            }
            if (this.mKeyboard.isShowing()) {
                return true;
            } else {
                this.mKeyboard.showPopupWindow(this);
                return true;
            }
        }
    }

    public interface InputAccountListener {
        void inputAccount(float value);
    }

    private View.OnDragListener scrollListener = new OnDragListener() {

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            if (DragEvent.ACTION_DRAG_STARTED == dragEvent.getAction()) {
                if (mKeyboard != null && mKeyboard.isShowing()) {
                    mKeyboard.close();
                    return false;
                }
            }
            return false;
        }
    };

    class KeyboardDialog extends PopupWindow implements OnClickListener, OnLongClickListener {
        Context mContext;
        private VirtualKeyboardView mKeyboardView;
        private PayAmountEdittext mEditText;
        //键盘键子集合
        private ArrayList<Map<String, String>> valueList;
        private boolean isHideDecimal = false;

        public KeyboardDialog(Context context, PayAmountEdittext editText, boolean isHideDecimal) {
            super(context);
            this.mEditText = editText;
            this.mContext = context;
            this.isHideDecimal = isHideDecimal;
            this.mKeyboardView = new VirtualKeyboardView(context);
            if (isHideDecimal) {
                this.mKeyboardView.hideDecimal();
            }
            //初始化键盘键子集合
            valueList = mKeyboardView.getValueList();
            this.setContentView(this.mKeyboardView);
            this.setFocusable(false);
            this.setOutsideTouchable(false);
            this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
            this.initKeyboardListener();
            this.update();
        }

        public void showPopupWindow(View parent) {
            if (!this.isShowing()) {
                this.showAsDropDown(parent, 0, 20);
            }
        }

        public void close() {
            this.dismiss();
        }

        private void initKeyboardListener() {
            mKeyboardView.getLayoutBack().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    close();
                }
            });


            this.mKeyboardView.getGridView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String amount = mEditText.getText().toString().trim();
                    int selection = mEditText.getSelectionStart();
                    if (position < 11 && position != 9) {    //点击0-9按钮
                        //数据插入
                        if (selection <= amount.length() - 1) {
                            if (!amount.contains(".")) {
                                StringBuilder stringBuilder = new StringBuilder(amount);
                                amount = stringBuilder.insert(selection, valueList.get(position).get("name")).toString();
                                mEditText.setText(amount);
                                mEditText.setSelection(selection + 1);
                            } else {
                                //在小数点前面插入
                                if (selection <= amount.indexOf(".")) {
                                    if (amount.equals("0") || amount.equals("0.")) {
                                        return;
                                    }
                                    StringBuilder stringBuilder = new StringBuilder(amount);
                                    amount = stringBuilder.insert(selection, valueList.get(position).get("name")).toString();
                                    mEditText.setText(amount);
                                    mEditText.setSelection(selection + 1);
                                    return;
                                } else {//在小数点后面插入
                                    if (amount.length() - amount.indexOf(".") >= 3) { //小数点后已有两位则不允许输入
                                        return;
                                    }
                                    StringBuilder stringBuilder = new StringBuilder(amount);
                                    amount = stringBuilder.insert(selection, valueList.get(position).get("name")).toString();
                                    mEditText.setText(amount);
                                    mEditText.setSelection(selection + 1);
                                }
                            }
                        } else {
                            //第一位不允许输入0
                            if (amount.length() == 0 && valueList.get(position).get("name").equals("0")) {
                                if (isHideDecimal) {
                                    return;
                                }
                                mEditText.setText("0.");
                                mEditText.setSelection(selection + 2);
                                return;
                            }
                            if (amount.contains(".")) {
                                if (amount.length() - amount.indexOf(".") >= 3) { //小数点后已有两位则不允许输入
                                    return;
                                }
                            }
                            amount = amount + valueList.get(position).get("name");
                            mEditText.setText(amount);
                            Editable ea = mEditText.getText();
                            mEditText.setSelection(ea.length());
                        }

                    } else if (position == 9) {      //点击小数点
                        if (isHideDecimal) {
                            return;
                        }
                        //第一位输入小数点
                        if (amount.length() == 0) {
                            mEditText.setText("0.");
                            mEditText.setSelection(selection + 2);
                            return;
                        }
                        //输入数据没有小数点
                        if (!amount.contains(".")) {
                            //数据插入
                            if (selection <= amount.length() - 1) {
                                StringBuilder stringBuilder = new StringBuilder(amount);
                                amount = stringBuilder.insert(selection, valueList.get(position).get("name")).toString();
                                if (amount.length() - amount.indexOf(".") >= 3) {
                                    amount = stringBuilder.delete(amount.indexOf(".") + 3, amount.length()).toString();
                                }
                                mEditText.setText(amount);
                                mEditText.setSelection(selection + 1);
                            } else {
                                amount = amount + valueList.get(position).get("name");
                                mEditText.setText(amount);
                                Editable ea = mEditText.getText();
                                mEditText.setSelection(ea.length());
                            }

                        }
                    } else if (position == 11) {      //点击退格键
                        if (selection <= 0) {
                            return;
                        }
                        if (amount.length() > 0) {
                            StringBuilder stringBuilder = new StringBuilder(amount);
                            amount = stringBuilder.delete(selection - 1, selection).toString();
                            if (amount.equals("0")) {
                                mEditText.setText("");
                                return;
                            }
                            if (amount.indexOf(".") == 0) {
                                amount = stringBuilder.deleteCharAt(0).toString();
                            }
                            mEditText.setText(amount);
                            mEditText.setSelection(selection - 1);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
