package com.pyding.deathlyhallows.hooks;

import com.emoniph.witchery.item.ItemMutator;

import java.lang.reflect.Method;

public class Hooks {
    public static void main(String[] args) {
        ItemMutator instance = new ItemMutator();
        try {
            Method method = ItemMutator.class.getMethod("onItemUseFirst");
            MethodHook hook = new MethodHook();
            hook.setOriginalMethod(method);
            hook.callHook(instance);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}

class MethodHook {
    private Method originalMethod;

    public void setOriginalMethod(Method method) {
        this.originalMethod = method;
    }

    public void callHook(Object instance) {
        System.out.println("Метод будет выполнен.");
        try {
            originalMethod.invoke(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Метод был успешно выполнен!");
    }
}
