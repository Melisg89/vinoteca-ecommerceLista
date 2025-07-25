package com.tequila.ecommerce.vinoteca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//gestiona la navegación entre páginas de la aplicación web del frontend.

@Controller //no devuelve JSON, sino vistas HTML

public class PageController { //responsable de redirigir las URLs

    @GetMapping("/")
    //Cada uno de estos métodos captura una ruta, y redirige internamente hacia un archivo .html
    //usando forward:/archivo.html.

    public String index() { //"forward: redirige internamente al recurso HTML
        return "forward:/index.html";
    }

    @GetMapping("/about")
    public String about() {
        return "forward:/about.html";
    }

    @GetMapping("/product")
    public String product() {
        return "forward:/product.html";
    }

    @GetMapping("/product-single")
    public String productSingle() {
        return "forward:/product-single.html";
    }

    @GetMapping("/cart")
    public String cart() {
        return "forward:/cart.html";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "forward:/checkout.html";
    }

    @GetMapping("/blog")
    public String blog() {
        return "forward:/blog.html";
    }

    @GetMapping("/blog-single")
    public String blogSingle() {
        return "forward:/blog-single.html";
    }

    @GetMapping("/contact")
    public String contact() {
        return "forward:/contact.html";
    }

    @GetMapping("/main")
    public String main() {
        return "forward:/main.html";
    }
}