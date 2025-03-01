package com.example.Controller;
import com.example.Entity.Pedido;
import com.example.Entity.MpPreference;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/apiMp")
@CrossOrigin("*")
public class MercadoPagoController {

    @PostMapping
    public MpPreference getList(@RequestBody Pedido pedido) {

        /*
        *  List<PreferenceItemRequest> items = new ArrayList<>();
        for (PedidoDetalleDto detalle : pedido.getPedidosDetalle()) {
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title(detalle.getTitle())
                    .quantity(detalle.getQuantity())
                    .unitPrice(new BigDecimal(detalle.getUnitPrice()))
                    .build();
            items.add(itemRequest);
        }
        *
        * */



        try {
            MercadoPagoConfig.setAccessToken("TEST-2518292404065085-061315-8785b82d0948f8899243cf6075b1f1f7-511635406");

            //Creamos la preferencia060614
            //PREFERENCIA DE VENTA
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id("1234")//id hardcodeado
                    .title("compra producto")
                    .description("Pedido realizado desde el carrito de compras")
                    .pictureUrl("https://acdn.mitiendanube.com/stores/813/752/products/spruce41-31-d9af8e704d16b81a9216426267078691-1024-1024.jpg")
                    .quantity(1)
                    .currencyId("ARG")
                    .unitPrice(new BigDecimal(pedido.getTotalPedido()))
                    .build();
            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            //preferencia de control de sucesos en el caso que toque lo redirecciona a otra pagna
            //aca no pueden ir url localesm, pero hacemos una excepcion
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("http://localhost:5173/instrumentos")
                    .pending("http://localhost:5173/instrumentos")
                    .failure("http://localhost:5173/instrumentos")
                    .build();

            //preferencia que tendra todas las preferencias que se hayan creado
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .build();

            // creo un cliente para comunicarme con mp
            PreferenceClient client = new PreferenceClient();
            //se crea una nueeva prefertencia que es igual a lla respuesta
            Preference preference = client.create(preferenceRequest);


            MpPreference mpPreference = new MpPreference();
            mpPreference.setStatusCode(preference.getResponse().getStatusCode());
            mpPreference.setId(preference.getId());
            return mpPreference;


        } catch (MPException e) {
            throw new RuntimeException(e);

        } catch (MPApiException e) {
            throw new RuntimeException(e);
        }
    }
}
