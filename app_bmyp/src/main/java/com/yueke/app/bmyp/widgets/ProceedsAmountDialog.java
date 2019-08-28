package com.yueke.app.bmyp.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.askia.keyboard.widget.VirtualKeyboardView;
import com.yueke.app.bmyp.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public class ProceedsAmountDialog extends PopupWindow implements View.OnClickListener {

    private OnAmountInputFinishListener onAmountInputFinishListener;
    private Activity mContext;
    private View mMenuView;
    private VirtualKeyboardView virtualKeyboardView;
    private EditText mEditText;
    private Button button;
    //键盘键子集合
    private ArrayList<Map<String, String>> valueList;

    public ProceedsAmountDialog(Context context, OnAmountInputFinishListener onAmountInputFinishListener) {
        super(context);
        this.mContext = (Activity) context;
        this.onAmountInputFinishListener = onAmountInputFinishListener;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.setting_amount_layout, null);
        mMenuView.findViewById(R.id.bt_close).setOnClickListener(this);
        button = mMenuView.findViewById(R.id.bt_confirm);
        button.setOnClickListener(this);
        mEditText = mMenuView.findViewById(R.id.et_payAmount);
        mEditText.addTextChangedListener(textWatcher);
        virtualKeyboardView = mMenuView.findViewById(R.id.virtualKeyboardView);
        virtualKeyboardView.getLayoutBack().setVisibility(View.GONE);
        //初始化键盘键子集合
        valueList = virtualKeyboardView.getValueList();
        this.setContentView(mMenuView);
        this.setFocusable(false);
        this.setOutsideTouchable(false);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
        this.initKeyboardListener();
        hideSoftInpuMethod();
        this.update();
    }

    private void hideSoftInpuMethod() {
        // 设置不调用系统键盘
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            mEditText.setInputType(InputType.TYPE_NULL);
        } else {
            ((Activity) this.mContext).getWindow().setSoftInputMode(
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

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence text, int start, int i1, int lengthAfter) {
            if (TextUtils.isEmpty(text)) {
                button.setEnabled(false);
                return;
            }
            //以小数点开始或者结尾
            if (text.toString().endsWith(".") || text.toString().startsWith(".")) {
                button.setEnabled(false);
                return;
            }

            //金额小于0
            if (Double.valueOf(text.toString()) < 0) {
                //在监听Edittext内容时 通过改变实体类的值无法改变Edittext内容   mRechargeViewModel.getPaymentMethod().getPayAmount().set("");
                button.setEnabled(false);
                return;
            }
            button.setEnabled(true);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_close:
                close();
                break;
            case R.id.bt_confirm:
                if (onAmountInputFinishListener != null) {
                    onAmountInputFinishListener.inputFinish(mEditText.getText().toString());
                }
                break;
        }
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    public void close() {
        mEditText.setText("");
        this.dismiss();
    }

    private void initKeyboardListener() {
        this.virtualKeyboardView.getGridView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String amount = mEditText.getText().toString().trim();
                int selection = mEditText.getSelectionStart();
                if (position < 11 && position != 9) {    //点击0-9按钮
                    //LogUtils.d("selection:" + selection + "||indexOF:" + amount.indexOf("."));
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

    public interface OnAmountInputFinishListener {
        void inputFinish(String amount);
    }

}
