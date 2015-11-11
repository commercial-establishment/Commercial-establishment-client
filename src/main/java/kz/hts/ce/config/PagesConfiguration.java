/*
 * Copyright (c) 2012, Stephen Chin
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL STEPHEN CHIN OR ORACLE CORPORATION BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package kz.hts.ce.config;

import javafx.stage.Stage;
import kz.hts.ce.controller.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import static kz.hts.ce.util.SpringFxmlLoader.showStage;

@Configuration
@Lazy
public class PagesConfiguration {

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Bean
    @Scope("prototype")
    public Stage main() {
        showStage(primaryStage, "/view/main.fxml");
        return primaryStage;
    }

    @Bean
    @Scope("prototype")
    public Stage login() {
        showStage(primaryStage, "/view/login.fxml");
        return primaryStage;
    }

    @Bean
    @Scope("prototype")
    public Stage addProduct() {
        showStage(primaryStage, "/view/add-product.fxml");
        return primaryStage;
    }

    @Bean
    @Scope("prototype")
    public Stage payment() {
        showStage(primaryStage, "/view/payment.fxml");
        return primaryStage;
    }

    @Bean
    @Scope("prototype")
    public Stage products() {
        showStage(primaryStage, "/view/products.fxml");
        return primaryStage;
    }

    @Bean
    @Scope("prototype")
    public LoginController loginController() {
        return new LoginController();
    }

    @Bean
    @Scope("prototype")
    public MainController mainController() {
        return new MainController();
    }

    @Bean
    @Scope("prototype")
    public CalculatorController calculatorController() {
        return new CalculatorController();
    }

    @Bean
    @Scope("prototype")
    public PaymentController paymentController() {
        return new PaymentController();
    }

    @Bean
    @Scope("prototype")
    public ProductsController productsController() {
        return new ProductsController();
    }
}
