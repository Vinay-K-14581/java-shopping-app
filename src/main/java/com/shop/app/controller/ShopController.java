package com.shop.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ShopController {

    record Product(String name, String description, String price, String availability) {}

    @GetMapping("/")
    public String home(Model model) {
        List<Product> products = List.of(
                new Product("Mobile", "Latest 5G smartphone with AMOLED display", "₹19,999", "In Stock"),
                new Product("Laptop", "Lightweight laptop for work and gaming", "₹59,999", "Limited Stock"),
                new Product("Headphones", "Noise-cancelling wireless headphones", "₹2,999", "In Stock")
        );

        model.addAttribute("products", products);
        model.addAttribute("deployMessage", "Deployed successfully using Jenkins + Docker + Kubernetes (Minikube)");
        return "index";
    }
}
