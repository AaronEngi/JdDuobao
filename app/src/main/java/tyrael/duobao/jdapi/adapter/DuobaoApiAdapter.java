package tyrael.duobao.jdapi.adapter;

import wang.tyrael.pattern.Singleton;

public class DuobaoApiAdapter extends Singleton<DuobaoApiAdapter> {

    @Override
    protected DuobaoApiAdapter create() {
        return new DuobaoApiAdapter();
    }
    
}
