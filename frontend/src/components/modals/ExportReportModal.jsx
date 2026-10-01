import React, { useState } from 'react';
import { X, Download, FileText, Code2, Check, Copy } from 'lucide-react';
import { useProject } from '../../context/ProjectContext';
import { useCompiler } from '../../context/CompilerContext';
import { generateMarkdownReport, downloadFile } from '../../utils/reportGenerator';

export const ExportReportModal = ({ isOpen, onClose }) => {
  const { activeProject, activeFile } = useProject();
  const { compilerResult } = useCompiler();
  const [copied, setCopied] = useState(false);

  if (!isOpen) return null;

  const markdownReport = generateMarkdownReport(activeProject, activeFile, compilerResult);

  const handleDownloadMarkdown = () => {
    const fileName = `${activeFile?.name?.replace(/\.[^/.]+$/, "") || 'compiler'}_analysis_report.md`;
    downloadFile(markdownReport, fileName, 'text/markdown');
  };

  const handleDownloadJson = () => {
    const payload = {
      project: activeProject,
      file: activeFile,
      result: compilerResult,
      generatedAt: new Date().toISOString()
    };
    const fileName = `${activeFile?.name?.replace(/\.[^/.]+$/, "") || 'compiler'}_analysis.json`;
    downloadFile(JSON.stringify(payload, null, 2), fileName, 'application/json');
  };

  const handleCopyMarkdown = () => {
    navigator.clipboard.writeText(markdownReport);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div className="bg-[#161b22] border border-[#30363d] rounded-lg max-w-2xl w-full p-4 shadow-2xl flex flex-col max-h-[85vh]">
        <div className="flex items-center justify-between pb-3 border-b border-[#21262d]">
          <div className="flex items-center space-x-2 text-sm font-semibold text-white">
            <Download className="w-4 h-4 text-indigo-400" />
            <span>Export Compiler Analysis Report</span>
          </div>
          <button onClick={onClose} className="text-[#8b949e] hover:text-white">
            <X className="w-4 h-4" />
          </button>
        </div>

        <div className="my-3 flex-1 overflow-auto bg-[#0d1117] p-3 rounded border border-[#30363d] font-mono text-xs text-[#c9d1d9] whitespace-pre-wrap select-text">
          {markdownReport}
        </div>

        <div className="flex items-center justify-between pt-3 border-t border-[#21262d] text-xs">
          <button
            onClick={handleCopyMarkdown}
            className="px-3 py-1.5 rounded bg-[#21262d] hover:bg-[#30363d] text-[#c9d1d9] border border-[#30363d] flex items-center gap-1.5 transition-colors"
          >
            {copied ? <Check className="w-3.5 h-3.5 text-emerald-400" /> : <Copy className="w-3.5 h-3.5" />}
            <span>{copied ? 'Copied Markdown' : 'Copy Markdown'}</span>
          </button>

          <div className="flex items-center space-x-2">
            <button
              onClick={handleDownloadJson}
              className="px-3 py-1.5 rounded bg-[#21262d] hover:bg-[#30363d] text-[#c9d1d9] border border-[#30363d] flex items-center gap-1.5 transition-colors"
            >
              <Code2 className="w-3.5 h-3.5" />
              <span>Download JSON</span>
            </button>
            <button
              onClick={handleDownloadMarkdown}
              className="px-3 py-1.5 rounded bg-indigo-600 hover:bg-indigo-500 text-white font-medium flex items-center gap-1.5 transition-colors"
            >
              <FileText className="w-3.5 h-3.5" />
              <span>Download Markdown Report</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
