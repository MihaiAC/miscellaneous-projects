fn main() {
    let a: u8 = 250;
    let b: u8 = 10;

    match a.checked_add(b) {
        Some(result) => println!("Checked: {}", result),
        None => println!("Checked: overflow"),
    }

    let wrap = a.wrapping_add(b);
    println!("Wrapping: {}", wrap);

    let sat = a.saturating_add(b);
    println!("Saturating: {}", sat);

    let (result, did_overflow) = a.overflowing_add(b);
    println!("Overflowing: {}, overflowed: {}", result, did_overflow);
}
