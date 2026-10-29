package com.vcube.stockmarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vcube.stockmarket.entity.Stock;
import com.vcube.stockmarket.repository.StockRepository;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')") 
public class AdminController {

    @Autowired
    private StockRepository stockRepository;

 // List all stocks
    @GetMapping("/stocks")
    public String listStocks(Model model) {
        model.addAttribute("stocks", stockRepository.findAll());
        return "admin/stocks";  // ✅ folder path added
    }

    //  Add new stock form
    @GetMapping("/stocks/new")
    public String newStockForm(Model model) {
        model.addAttribute("stock", new Stock());
        return "admin/stock-form";  // ✅ folder path added
    }

    //  Save new stock
    @PostMapping("/stocks")
    public String saveStock(@ModelAttribute Stock stock) {
        stockRepository.save(stock);
        return "redirect:/admin/stocks"; // ✅ redirect unchanged
    }

    //  Edit existing stock form
    @GetMapping("/stocks/edit/{id}")
    public String editStockForm(@PathVariable Long id, Model model) {
        Stock stock = stockRepository.findById(id).orElseThrow();
        model.addAttribute("stock", stock);
        return "admin/stock-form";  // ✅ folder path added
    }

    // Update stock
    @PostMapping("/stocks/update/{id}")
    public String updateStock(@PathVariable Long id, @ModelAttribute Stock stockDetails) {
        Stock stock = stockRepository.findById(id).orElseThrow();

        stock.setStockName(stockDetails.getStockName());
        stock.setStockSymbol(stockDetails.getStockSymbol());
        stock.setCurrentPrice(stockDetails.getCurrentPrice());

        stockRepository.save(stock);
        return "redirect:/admin/stocks"; // ✅ redirect unchanged
    }
    
 // Delete stock
    @GetMapping("/stocks/delete/{id}")
    public String deleteStock(@PathVariable Long id) {
        stockRepository.deleteById(id);
        return "redirect:/admin/stocks";
    }


}
