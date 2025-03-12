document.addEventListener("DOMContentLoaded", function () {
    fetch("manageProducts")
        .then(response => response.text())
        .then(data => {
            document.getElementById("product-grid").innerHTML = data;
        })
        .catch(error => console.error("Error loading products:", error));
});
