const fs = require('fs');
const content = fs.readFileSync('app/src/main/java/com/example/MainActivity.kt', 'utf8');
const lines = content.split('\n');
let count = 0;
for (let i = 0; i < lines.length; i++) {
    const line = lines[i];
    let cleanLine = line.replace(/".*?"/g, '""').replace(/\/\/.*/, '');
    for (let char of cleanLine) {
        if (char === '{') count++;
        if (char === '}') count--;
    }
    if (count === 0 && cleanLine.includes('}')) {
        console.log('Root closed at line ' + (i + 1));
    }
    if (count < 0) {
        console.log('Negative brace count at line ' + (i + 1) + '! count is ' + count);
        // don't reset, let's see how deep it goes
    }
}
console.log('Final count: ' + count);
