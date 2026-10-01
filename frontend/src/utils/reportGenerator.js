export function generateMarkdownReport(project, file, compilerResult) {
  const timestamp = new Date().toLocaleString();
  const success = compilerResult?.success;
  const statusStr = success ? 'SUCCESS' : 'FAILED';
  const duration = compilerResult?.totalExecutionTimeMs || 0;
  const errors = compilerResult?.totalErrors || 0;

  let md = `# ☁ Cloud Compiler Analysis Report

**Project:** ${project?.name || 'Playground'}  
**File:** ${file?.name || 'code.c'}  
**Generated At:** ${timestamp}  
**Compilation Status:** ${statusStr}  
**Total Errors:** ${errors}  
**Execution Time:** ${duration} ms  

---

## 1. Source Code
\`\`\`c
${file?.content || ''}
\`\`\`

---

## 2. Compilation Stages Summary
| Stage | Status | Duration (ms) | Summary |
|-------|--------|---------------|---------|
`;

  (compilerResult?.stages || []).forEach(stage => {
    md += `| ${stage.stageName} | ${stage.status} | ${stage.durationMs} | ${stage.message} |\n`;
  });

  md += `\n---\n\n## 3. Lexical Analysis (${compilerResult?.tokens?.length || 0} Tokens)\n\n`;
  md += `| Index | Category | Type | Lexeme | Line | Column |\n`;
  md += `|-------|----------|------|--------|------|--------|\n`;
  (compilerResult?.tokens || []).slice(0, 50).forEach((t, i) => {
    md += `| ${i + 1} | ${t.category} | ${t.type} | \`${t.lexeme}\` | ${t.line} | ${t.column} |\n`;
  });

  if ((compilerResult?.syntaxErrors?.length || 0) > 0) {
    md += `\n---\n\n## 4. Syntax Errors (${compilerResult.syntaxErrors.length})\n\n`;
    compilerResult.syntaxErrors.forEach((err, i) => {
      md += `${i + 1}. **Line ${err.line}, Col ${err.column}:** ${err.message} (Found: \`${err.found}\`)\n`;
    });
  }

  if ((compilerResult?.semanticErrors?.length || 0) > 0) {
    md += `\n---\n\n## 5. Semantic Errors (${compilerResult.semanticErrors.length})\n\n`;
    compilerResult.semanticErrors.forEach((err, i) => {
      md += `${i + 1}. **Line ${err.line}, Col ${err.column}:** [${err.errorType}] ${err.message}\n`;
    });
  }

  md += `\n---\n\n## 6. Symbol Table (${compilerResult?.symbolTable?.length || 0} Symbols)\n\n`;
  md += `| Name | Type | Scope | Line | Bytes | Reference Count |\n`;
  md += `|------|------|-------|------|-------|-----------------|\n`;
  (compilerResult?.symbolTable || []).forEach(s => {
    md += `| \`${s.name}\` | ${s.type} | ${s.scopeName} (lvl ${s.scopeLevel}) | ${s.line} | ${s.sizeBytes} | ${s.referenceCount} |\n`;
  });

  md += `\n---\n\n## 7. Intermediate Code (Three-Address Code)\n\n\`\`\`text\n`;
  (compilerResult?.threeAddressCode || []).forEach((tac, idx) => {
    md += `${idx}: ${tac.formatted || tac.toTacString || tac.op}\n`;
  });
  md += `\`\`\`\n\n`;

  md += `\n---\n\n## 8. Quadruples Representation\n\n`;
  md += `| Index | Op | Arg1 | Arg2 | Result |\n`;
  md += `|-------|----|------|------|--------|\n`;
  (compilerResult?.quadruples || []).forEach((q, i) => {
    md += `| ${i} | ${q.op} | ${q.arg1} | ${q.arg2} | ${q.result} |\n`;
  });

  md += `\n---\n\n## 9. Triples Representation\n\n`;
  md += `| Index | Op | Arg1 | Arg2 |\n`;
  md += `|-------|----|------|------|\n`;
  (compilerResult?.triples || []).forEach((t, i) => {
    md += `| (${i}) | ${t.op} | ${t.arg1} | ${t.arg2} |\n`;
  });

  return md;
}

export function downloadFile(content, filename, contentType = 'text/plain') {
  const blob = new Blob([content], { type: contentType });
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  URL.revokeObjectURL(url);
}
