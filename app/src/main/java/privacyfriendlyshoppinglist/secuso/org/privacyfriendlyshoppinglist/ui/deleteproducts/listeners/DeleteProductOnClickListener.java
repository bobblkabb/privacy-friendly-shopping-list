package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deleteproducts.listeners;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.AbstractInstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.InstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.utils.MessageUtils;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.ProductService;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.domain.ProductDto;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deleteproducts.DeleteProductsCache;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.main.MainActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.products.ProductsActivity;
import rx.Observable;

import java.util.List;

/**
 * Description:
 * Author: Grebiel Jose Ifill Brito
 * Created: 21.07.16 creation date
 */
public class DeleteProductOnClickListener implements View.OnClickListener
{
    private ProductService productService;
    private DeleteProductsCache cache;

    public DeleteProductOnClickListener(DeleteProductsCache cache)
    {
        this.cache = cache;
        AbstractInstanceFactory instanceFactory = new InstanceFactory(cache.getActivity().getApplicationContext());
        this.productService = (ProductService) instanceFactory.createInstance(ProductService.class);
    }

    @Override
    public void onClick(View v)
    {
        MessageUtils.showAlertDialog(cache.getActivity(), R.string.delete_confirmation_title, R.string.delete_products_confirmation, deleteProducts());
    }

    private Observable<Void> deleteProducts()
    {
        Observable<Void> observable = Observable
                .create(subscriber ->
                {
                    subscriber.onNext(deleteProductsSync());
                    subscriber.onCompleted();
                });
        return observable;
    }

    private Void deleteProductsSync()
    {
        // delete products
        List<ProductDto> productList = cache.getDeleteProductsAdapter().getProductsList();
        productService.deleteSelected(productList);

        // go to products overview
        AppCompatActivity activity = cache.getActivity();
        Intent intent = new Intent(activity, ProductsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(MainActivity.LIST_ID_KEY, cache.getListId());
        activity.startActivity(intent);
        return null;
    }
}
