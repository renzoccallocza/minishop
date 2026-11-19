package com.tecsup.minishop.controller;

import com.tecsup.minishop.model.Product;
import com.tecsup.minishop.repository.ProductRepository;
import com.tecsup.minishop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerDtoTest {

    @Test
    void createProduct_ignoraElIdEnviadoPorElCliente() throws Exception {
        // Arrange
        ProductRepository repository = mock(ProductRepository.class);
        when(repository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));
        ProductService service = new ProductService(repository);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new ProductController(service)).build();

        // Act
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":99,\"name\":\"Mouse\",\"price\":25.5,\"stock\":10}"))
                .andExpect(status().isCreated());

        // Assert
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(repository).save(captor.capture());
        assertNull(captor.getValue().getId());
        assertEquals("Mouse", captor.getValue().getName());
    }
}