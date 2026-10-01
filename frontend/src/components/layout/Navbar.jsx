import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { 
  Play, 
  Layers, 
  FileCode, 
  Settings, 
  FolderPlus, 
  FilePlus, 
  Download, 
  BookOpen, 
  LayoutDashboard, 
  History, 
  LogOut, 
  User as UserIcon, 
  CheckCircle2, 
  AlertTriangle, 
  Save, 
  ChevronDown,
  Sparkles
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useProject } from '../../context/ProjectContext';
import { useCompiler } from '../../context/CompilerContext';

export const Navbar = ({ onOpenNewProject, onOpenNewFile, onOpenTemplates, onOpenExport }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, isAuthenticated, logout } = useAuth();
  const { activeProject, activeFile, saveFile, autosaveEnabled, setAutosaveEnabled } = useProject();
  const { isCompiling, runCompilation, compilerResult } = useCompiler();
  const [showUserMenu, setShowUserMenu] = useState(false);

  const isIdePage = location.pathname === '/';

  return (
    <header className="h-12 border-b border-[#21262d] bg-[#161b22] px-3 flex items-center justify-between z-30 select-none">
      {/* Brand & Mode Switcher */}
      <div className="flex items-center space-x-3">
        <Link to="/" className="flex items-center space-x-2 text-indigo-400 font-bold hover:text-indigo-300 transition-colors">
          <div className="w-7 h-7 rounded-lg bg-indigo-600/20 border border-indigo-500/30 flex items-center justify-center text-indigo-400">
            <Layers className="w-4 h-4" />
          </div>
          <span className="text-sm font-semibold tracking-wide text-white flex items-center gap-1.5">
            CloudCompiler <span className="text-[10px] uppercase font-mono px-1.5 py-0.5 rounded bg-indigo-900/60 text-indigo-300 border border-indigo-700/40">IDE</span>
          </span>
        </Link>

        <div className="h-4 w-[1px] bg-[#30363d] mx-1" />

        {/* Navigation links */}
        <nav className="flex items-center space-x-1">
          <Link
            to="/"
            className={`px-2.5 py-1 text-xs font-medium rounded-md transition-colors flex items-center gap-1.5 ${
              isIdePage
                ? 'bg-[#21262d] text-white'
                : 'text-[#8b949e] hover:text-[#c9d1d9] hover:bg-[#21262d]/50'
            }`}
          >
            <FileCode className="w-3.5 h-3.5" />
            IDE
          </Link>
          <Link
            to="/dashboard"
            className={`px-2.5 py-1 text-xs font-medium rounded-md transition-colors flex items-center gap-1.5 ${
              location.pathname === '/dashboard'
                ? 'bg-[#21262d] text-white'
                : 'text-[#8b949e] hover:text-[#c9d1d9] hover:bg-[#21262d]/50'
            }`}
          >
            <LayoutDashboard className="w-3.5 h-3.5" />
            Dashboard
          </Link>
          <Link
            to="/history"
            className={`px-2.5 py-1 text-xs font-medium rounded-md transition-colors flex items-center gap-1.5 ${
              location.pathname === '/history'
                ? 'bg-[#21262d] text-white'
                : 'text-[#8b949e] hover:text-[#c9d1d9] hover:bg-[#21262d]/50'
            }`}
          >
            <History className="w-3.5 h-3.5" />
            History
          </Link>
        </nav>
      </div>

      {/* Middle: Actions when on IDE */}
      {isIdePage && (
        <div className="flex items-center space-x-2">
          {/* Quick templates */}
          <button
            onClick={onOpenTemplates}
            className="px-2.5 py-1 text-xs font-medium rounded bg-[#21262d] hover:bg-[#30363d] text-[#c9d1d9] border border-[#30363d] flex items-center gap-1.5 transition-colors"
            title="Load sample compiler code"
          >
            <Sparkles className="w-3.5 h-3.5 text-amber-400" />
            <span>Templates</span>
          </button>

          {/* Save Button */}
          {activeFile && (
            <button
              onClick={() => saveFile(activeFile.id)}
              className={`px-2.5 py-1 text-xs font-medium rounded flex items-center gap-1.5 border transition-colors ${
                activeFile.isDirty
                  ? 'bg-amber-500/10 border-amber-500/40 text-amber-300 hover:bg-amber-500/20'
                  : 'bg-[#21262d] border-[#30363d] text-[#8b949e] hover:bg-[#30363d]'
              }`}
              title="Save source file (Ctrl+S)"
            >
              <Save className="w-3.5 h-3.5" />
              <span>{activeFile.isDirty ? 'Save*' : 'Saved'}</span>
            </button>
          )}

          {/* Compile Button */}
          <button
            onClick={() => runCompilation('ALL')}
            disabled={isCompiling || !activeFile}
            className="px-3 py-1 text-xs font-semibold rounded bg-indigo-600 hover:bg-indigo-500 disabled:opacity-50 text-white shadow-sm flex items-center gap-1.5 transition-all"
            title="Compile & Run Analysis (Ctrl+Enter)"
          >
            <Play className={`w-3.5 h-3.5 fill-current ${isCompiling ? 'animate-spin' : ''}`} />
            <span>{isCompiling ? 'Compiling...' : 'Compile'}</span>
          </button>

          {/* Export Report */}
          <button
            onClick={onOpenExport}
            className="p-1.5 text-xs rounded bg-[#21262d] hover:bg-[#30363d] text-[#c9d1d9] border border-[#30363d]"
            title="Export Compiler Diagnostic Report"
          >
            <Download className="w-3.5 h-3.5" />
          </button>
        </div>
      )}

      {/* Right: User & Status */}
      <div className="flex items-center space-x-3">
        {/* Compiler Result Status Indicator */}
        {compilerResult && isIdePage && (
          <div className="hidden sm:flex items-center text-xs space-x-1.5 px-2 py-0.5 rounded bg-[#0d1117] border border-[#21262d]">
            {compilerResult.success ? (
              <span className="flex items-center text-emerald-400 gap-1">
                <CheckCircle2 className="w-3.5 h-3.5" />
                <span>Build Succeeded ({compilerResult.totalExecutionTimeMs}ms)</span>
              </span>
            ) : (
              <span className="flex items-center text-rose-400 gap-1">
                <AlertTriangle className="w-3.5 h-3.5" />
                <span>{compilerResult.totalErrors} Error(s)</span>
              </span>
            )}
          </div>
        )}

        {/* User Account / Auth Dropdown */}
        {isAuthenticated ? (
          <div className="relative">
            <button
              onClick={() => setShowUserMenu(!showUserMenu)}
              className="flex items-center space-x-2 px-2.5 py-1 rounded bg-[#21262d] hover:bg-[#30363d] text-xs font-medium text-[#c9d1d9] border border-[#30363d] transition-colors"
            >
              <div className="w-4 h-4 rounded-full bg-indigo-500 flex items-center justify-center text-[10px] text-white font-bold">
                {user?.name?.charAt(0).toUpperCase() || 'U'}
              </div>
              <span className="max-w-[90px] truncate">{user?.name}</span>
              <ChevronDown className="w-3 h-3 text-[#8b949e]" />
            </button>

            {showUserMenu && (
              <div className="absolute right-0 mt-1 w-48 bg-[#161b22] border border-[#30363d] rounded-md shadow-xl py-1 z-50">
                <div className="px-3 py-2 border-b border-[#21262d]">
                  <p className="text-xs font-semibold text-white truncate">{user?.name}</p>
                  <p className="text-[11px] text-[#8b949e] truncate">{user?.email}</p>
                </div>
                <button
                  onClick={() => {
                    setShowUserMenu(false);
                    navigate('/dashboard');
                  }}
                  className="w-full text-left px-3 py-1.5 text-xs text-[#c9d1d9] hover:bg-[#21262d] flex items-center gap-2"
                >
                  <LayoutDashboard className="w-3.5 h-3.5" /> Dashboard
                </button>
                <button
                  onClick={() => {
                    setShowUserMenu(false);
                    navigate('/history');
                  }}
                  className="w-full text-left px-3 py-1.5 text-xs text-[#c9d1d9] hover:bg-[#21262d] flex items-center gap-2"
                >
                  <History className="w-3.5 h-3.5" /> History
                </button>
                <div className="border-t border-[#21262d] my-1" />
                <button
                  onClick={() => {
                    setShowUserMenu(false);
                    logout();
                  }}
                  className="w-full text-left px-3 py-1.5 text-xs text-rose-400 hover:bg-rose-500/10 flex items-center gap-2"
                >
                  <LogOut className="w-3.5 h-3.5" /> Sign Out
                </button>
              </div>
            )}
          </div>
        ) : (
          <div className="flex items-center space-x-2">
            <Link
              to="/login"
              className="px-2.5 py-1 text-xs font-medium rounded text-[#c9d1d9] hover:bg-[#21262d] transition-colors"
            >
              Sign In
            </Link>
            <Link
              to="/register"
              className="px-2.5 py-1 text-xs font-medium rounded bg-indigo-600 hover:bg-indigo-500 text-white transition-colors"
            >
              Register
            </Link>
          </div>
        )}
      </div>
    </header>
  );
};
