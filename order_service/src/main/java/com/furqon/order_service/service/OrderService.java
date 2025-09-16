package com.furqon.order_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.furqon.order_service.model.Order;
import com.furqon.order_service.repository.OrderRepository;
import com.furqon.order_service.vo.Pelanggan;
import com.furqon.order_service.vo.Product;
import com.furqon.order_service.vo.ResponseTemplate;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private DiscoveryClient discoveryClient;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<ResponseTemplate> getOrderWithProductById(Long id) {
        List<ResponseTemplate> responseList = new ArrayList<>();
        Order order = getOrderById(id);
        if (order == null) {
            return null;
        }

        ServiceInstance serviceInstance = discoveryClient.getInstances("API-GATEWAY").get(0);
        Product product = restTemplate
                .getForObject(serviceInstance.getUri() + "/api/product/" + order.getProductId(), Product.class);
        Pelanggan pelanggan = restTemplate.getForObject(
                serviceInstance.getUri() + "/api/pelanggan/" + order.getPelangganId(), Pelanggan.class);

        ResponseTemplate vo = new ResponseTemplate();
        vo.setOrder(order);
        vo.setProduct(product);
        vo.setPelanggan(pelanggan);
        responseList.add(vo);

        return responseList;
    }
}
