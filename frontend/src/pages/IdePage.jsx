import React, { useState } from 'react';
import { Navbar } from '../components/layout/Navbar';
import { Sidebar } from '../components/layout/Sidebar';
import { Footer } from '../components/layout/Footer';
import { FileTabBar } from '../components/editor/FileTabBar';
import { EditorHeader } from '../components/editor/EditorHeader';
import { MonacoCodeEditor } from '../components/editor/MonacoCodeEditor';
import { OutputPanel } from '../components/panels/OutputPanel';
import { NewProjectModal } from '../components/modals/NewProjectModal';
import { NewFileModal } from '../components/modals/NewFileModal';
import { ExportReportModal } from '../components/modals/ExportReportModal';
import { TemplatesModal } from '../components/modals/TemplatesModal';

export const IdePage = () => {
  const [isNewProjectOpen, setIsNewProjectOpen] = useState(false);
  const [isNewFileOpen, setIsNewFileOpen] = useState(false);
  const [isTemplatesOpen, setIsTemplatesOpen] = useState(false);
  const [isExportOpen, setIsExportOpen] = useState(false);

  return (
    <div className="h-screen w-screen flex flex-col bg-[#0d1117] text-[#c9d1d9] overflow-hidden">
      {/* Top Navbar */}
      <Navbar
        onOpenNewProject={() => setIsNewProjectOpen(true)}
        onOpenNewFile={() => setIsNewFileOpen(true)}
        onOpenTemplates={() => setIsTemplatesOpen(true)}
        onOpenExport={() => setIsExportOpen(true)}
      />

      {/* Main Workspace */}
      <div className="flex-1 flex overflow-hidden">
        {/* Left Sidebar */}
        <Sidebar
          onOpenNewProject={() => setIsNewProjectOpen(true)}
          onOpenNewFile={() => setIsNewFileOpen(true)}
          onOpenTemplates={() => setIsTemplatesOpen(true)}
        />

        {/* Center Editor Area */}
        <div className="flex-1 flex flex-col border-r border-[#21262d] overflow-hidden">
          <FileTabBar onOpenNewFile={() => setIsNewFileOpen(true)} />
          <EditorHeader />
          <div className="flex-1 overflow-hidden relative">
            <MonacoCodeEditor />
          </div>
        </div>

        {/* Right Output Panel */}
        <div className="w-1/2 lg:w-[48%] xl:w-[45%] flex flex-col overflow-hidden">
          <OutputPanel />
        </div>
      </div>

      {/* Bottom Footer Status Bar */}
      <Footer />

      {/* Dialog Modals */}
      <NewProjectModal
        isOpen={isNewProjectOpen}
        onClose={() => setIsNewProjectOpen(false)}
      />
      <NewFileModal
        isOpen={isNewFileOpen}
        onClose={() => setIsNewFileOpen(false)}
      />
      <TemplatesModal
        isOpen={isTemplatesOpen}
        onClose={() => setIsTemplatesOpen(false)}
      />
      <ExportReportModal
        isOpen={isExportOpen}
        onClose={() => setIsExportOpen(false)}
      />
    </div>
  );
};
