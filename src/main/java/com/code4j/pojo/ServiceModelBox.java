package com.code4j.pojo;

/**
 * @author lwp
 * @date 2022-06-20
 */
public class ServiceModelBox {
    private ModelBoxInfo serviceApi;
    private ModelBoxInfo serviceImpl;

    public ServiceModelBox(ModelBoxInfo serviceApi, ModelBoxInfo serviceImpl) {
        this.serviceApi = serviceApi;
        this.serviceImpl = serviceImpl;
    }

    public ModelBoxInfo getServiceApi() {
        return serviceApi;
    }

    public void setServiceApi(ModelBoxInfo serviceApi) {
        this.serviceApi = serviceApi;
    }

    public ModelBoxInfo getServiceImpl() {
        return serviceImpl;
    }

    public void setServiceImpl(ModelBoxInfo serviceImpl) {
        this.serviceImpl = serviceImpl;
    }
}
