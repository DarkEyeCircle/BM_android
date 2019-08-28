package com.yueke.app.bmyp.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseFragment;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.NetShoppingFragmentBinding;

@Route(path = ARouterPath.FragmentNetShopping)
public class NetShoppingFragment extends BaseFragment {

    private NetShoppingFragmentBinding mNetShoppingFragmentBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mNetShoppingFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.net_shopping_fragment, container, false);
        return mNetShoppingFragmentBinding.getRoot();
    }
}
