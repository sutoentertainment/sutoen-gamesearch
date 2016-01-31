package com.sutoen.sutogamesearch.injector.components;

import com.sutoen.sutogamesearch.fragments.MainActivityFragment;
import com.sutoen.sutogamesearch.injector.modules.NetworkModule;

import dagger.Component;

/**
 * Component to connect the Network module and its injections
 */
@Component(modules = { NetworkModule.class })
public interface NetworkComponent {
    void inject(MainActivityFragment fragment);
}
