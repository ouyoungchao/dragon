package com.shiliu.dragon.common.base;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface BaseMapper<T> extends tk.mybatis.mapper.common.BaseMapper<T>,
        ExampleMapper<T>, ConditionMapper<T>, IdsMapper<T>, InsertListMapper<T> {
}
