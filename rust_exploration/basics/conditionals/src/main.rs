fn main() {
    let number = 7;

    if number < 5 {
        println!("number was less than 5");
    } else if number > 5 {
        println!("number was greater than 5");
    } else {
        println!("number was equal to 5");
    }

    let condition = true;
    let number2 = if condition {5} else {6};
    println!("The value of number2 is: {number2}");

    // Error:
    // let number = if condition {5} else {"six"};

    let mut counter = 0;
    let result = loop {
        counter += 1;

        if counter == 10 {
            break counter * 2;
        }
    };

    println!("The result is {result}");

    println!("Breaking loops");
    let mut count = 0;

    'counting_up: loop {
        println!("count = {count}");
        let mut remaining = 10;

        loop {
            println!("remaining = {remaining}");

            if remaining == 9 {
                break;
            }

            if count == 2 {
                break 'counting_up;
            }

            remaining -= 1;
        }

        count += 1;
    }

    println!("End count = {count}");

    count = 0;
    println!("Counting with while");
    while count <= 5 {
        println!("{count}!");
        count += 1;
    }

    println!("Looping through array with for");
    let arr = [10, 20, 30, 40, 50];
    for element in arr {
        println!("The value is {element}");
    }

}
