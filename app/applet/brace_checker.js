const fs = require('fs');
const content = fs.readFileSync('app/src/main/java/com/example/MainActivity.kt', 'utf8');
const lines = content.split('\n');
const stack = [];
for (let i = 0; i < lines.length; i++) {
    const line = lines[i];
    let cleanLine = line.replace(/".*?"/g, '""').replace(/\/\/.*/, '');
    for (let char of cleanLine) {
        if (char === '{') stack.push({line: i + 1, char: '{'});
        if (char === '}') {
            if (stack.length > 0) stack.pop();
            else console.log('Extra closing brace at line', i + 1);
        }
    }
}
if (stack.length > 0) {
    console.log('Unclosed braces opened at lines:');
    stack.forEach(s => console.log(s.line));
} else {
    console.log('Braces are perfectly balanced!');
}
