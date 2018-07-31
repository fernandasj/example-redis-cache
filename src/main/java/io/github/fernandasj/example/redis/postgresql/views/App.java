package io.github.fernandasj.example.redis.postgresql.views;

import com.google.gson.Gson;
import io.github.fernandasj.example.redis.postgresql.models.DaoProduto;
import io.github.fernandasj.example.redis.postgresql.models.Produto;
import redis.clients.jedis.Jedis;

/**
 *
 * @author fernanda
 */
public class App {
    public static void main(String[] args) {       
        
        DaoProduto daoProduto = new DaoProduto();
//        
//        Produto prod1 = new Produto(1, "Arroz", 2.5f);
//        daoProduto.save(prod1);
//        
//        Produto prod2 = new Produto(2, "Feijao", 4.5f);
//        daoProduto.save(prod2);
//        
//        Produto prod3 = new Produto(3, "Açucar", 1.5f);
//        daoProduto.save(prod3);
//        
//        Produto prod4 = new Produto(4, "Macarrão", 3.25f);
//        daoProduto.save(prod4);
        
        daoProduto.validateProduto(5);
    }
}
