package com.twinvest.wind.ports.inbound;
import com.twinvest.wind.domain.Asset;
import java.util.List;

public interface ListAssetsUseCase {
    List<Asset> listAssets();
}