package nl.tele2.wso2is.rest.factories;


import nl.tele2.wso2is.rest.CreateUpdateUserApiService;
import nl.tele2.wso2is.rest.impl.CreateUpdateUserApiImpl;

public class CreateUpdateUserFactory {
    private final static CreateUpdateUserApiService service = new CreateUpdateUserApiImpl();

    public static CreateUpdateUserApiService getCreateUpdateUserApi()
    {
        return service;
    }
}
