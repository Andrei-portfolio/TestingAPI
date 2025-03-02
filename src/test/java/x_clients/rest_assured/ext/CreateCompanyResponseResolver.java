package x_clients.rest_assured.ext;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import x_clients.rest_assured.entity.CreateCompanyResponse;
import x_clients.rest_assured.helpers.ApiCompanyHelper;

public class CreateCompanyResponseResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(CreateCompanyResponse.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        ApiCompanyHelper apiCompanyHelper = new ApiCompanyHelper();
        return apiCompanyHelper.createCompany("Entity company", "desc with entity");
    }
}
