function validateForm() {
    const productName = document.getElementById('productName').value;
    if (!productName) {
        alert('Please fill in the product name.');
        return false;
    }
    // Additional validation logic can go here
    return true;
}
