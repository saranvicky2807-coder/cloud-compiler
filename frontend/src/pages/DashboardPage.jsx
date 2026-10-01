import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  Folder, 
  FileCode, 
  Play, 
  CheckCircle2, 
  AlertCircle, 
  Clock, 
  TrendingUp, 
  Plus, 
  ChevronRight, 
  Sparkles,
  Layers
} from 'lucide-react';
import { Navbar } from '../components/layout/Navbar';
import { Footer } from '../components/layout/Footer';
import { projectService } from '../services/projectService';
import { useAuth } from '../context/AuthContext';
import { useProject } from '../context/ProjectContext';
import { SAMPLE_PROGRAMS } from '../utils/samplePrograms';

export const DashboardPage = () => {
  const navigate = useNavigate();
  const { isAuthenticated, user } = useAuth();
  const { selectProject, loadSampleProgram } = useProject();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      if (isAuthenticated) {
        try {
          const data = await projectService.getDashboardStats();
          setStats(data);
        } catch (err) {
          console.error('Failed to load stats:', err);
        }
      }
      setLoading(false);
    };
    fetchStats();
  }, [isAuthenticated]);

  const handleOpenProject = (project) => {
    selectProject(project);
    navigate('/');
  };

  const handleOpenTemplate = (template) => {
    loadSampleProgram(template);
    navigate('/');
  };

  return (
    <div className="h-screen w-screen flex flex-col bg-[#0d1117] text-[#c9d1d9] overflow-hidden">
      <Navbar />

      <div className="flex-1 overflow-y-auto p-6 max-w-6xl w-full mx-auto space-y-6">
        {/* Welcome Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-xl font-bold text-white flex items-center gap-2">
              <Layers className="w-5 h-5 text-indigo-400" />
              Compiler Engineering Dashboard
            </h1>
            <p className="text-xs text-[#8b949e] mt-0.5">
              {isAuthenticated
                ? `Welcome back, ${user?.name}! Track compiler metrics, runs, and workspace projects.`
                : 'Welcome to Cloud Compiler IDE Playground.'}
            </p>
          </div>

          <button
            onClick={() => navigate('/')}
            className="px-3 py-1.5 bg-indigo-600 hover:bg-indigo-500 text-white rounded text-xs font-semibold flex items-center gap-1.5 transition-colors"
          >
            <Play className="w-3.5 h-3.5 fill-current" />
            <span>Open IDE</span>
          </button>
        </div>

        {/* Statistics Cards */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div className="bg-[#161b22] border border-[#21262d] rounded-lg p-4">
            <div className="flex items-center justify-between text-[#8b949e] text-xs">
              <span>Total Projects</span>
              <Folder className="w-4 h-4 text-indigo-400" />
            </div>
            <div className="text-2xl font-bold text-white mt-2">
              {stats?.totalProjects || (isAuthenticated ? 0 : 1)}
            </div>
            <div className="text-[11px] text-[#484f58] mt-1">Active workspaces</div>
          </div>

          <div className="bg-[#161b22] border border-[#21262d] rounded-lg p-4">
            <div className="flex items-center justify-between text-[#8b949e] text-xs">
              <span>Total Compilations</span>
              <Play className="w-4 h-4 text-blue-400" />
            </div>
            <div className="text-2xl font-bold text-white mt-2">
              {stats?.totalCompilations || 0}
            </div>
            <div className="text-[11px] text-[#484f58] mt-1">Pipeline executions</div>
          </div>

          <div className="bg-[#161b22] border border-[#21262d] rounded-lg p-4">
            <div className="flex items-center justify-between text-[#8b949e] text-xs">
              <span>Success Rate</span>
              <TrendingUp className="w-4 h-4 text-emerald-400" />
            </div>
            <div className="text-2xl font-bold text-emerald-400 mt-2">
              {stats?.successRatePercentage !== undefined ? `${stats.successRatePercentage}%` : '100%'}
            </div>
            <div className="text-[11px] text-[#484f58] mt-1">Zero syntax & semantic errors</div>
          </div>

          <div className="bg-[#161b22] border border-[#21262d] rounded-lg p-4">
            <div className="flex items-center justify-between text-[#8b949e] text-xs">
              <span>Avg Execution Time</span>
              <Clock className="w-4 h-4 text-amber-400" />
            </div>
            <div className="text-2xl font-bold text-amber-300 mt-2">
              {stats?.averageExecutionTimeMs !== undefined ? `${stats.averageExecutionTimeMs} ms` : '12 ms'}
            </div>
            <div className="text-[11px] text-[#484f58] mt-1">Full pipeline traversal</div>
          </div>
        </div>

        {/* Section 2: Recent Projects & Quick Start Templates */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Recent Projects */}
          <div className="bg-[#161b22] border border-[#21262d] rounded-lg p-4 flex flex-col">
            <div className="flex items-center justify-between mb-3 pb-2 border-b border-[#21262d]">
              <span className="text-xs font-semibold text-white flex items-center gap-1.5">
                <Folder className="w-4 h-4 text-indigo-400" />
                Recent Projects
              </span>
            </div>

            <div className="flex-1 space-y-2">
              {stats?.recentProjects && stats.recentProjects.length > 0 ? (
                stats.recentProjects.map(proj => (
                  <div
                    key={proj.id}
                    onClick={() => handleOpenProject(proj)}
                    className="p-3 bg-[#0d1117] hover:bg-[#21262d] rounded border border-[#21262d] cursor-pointer flex items-center justify-between transition-colors group"
                  >
                    <div>
                      <div className="text-xs font-semibold text-indigo-300 group-hover:text-indigo-200">
                        {proj.name}
                      </div>
                      <div className="text-[11px] text-[#8b949e] mt-0.5">
                        {proj.files?.length || 0} file(s) • {new Date(proj.updatedAt).toLocaleDateString()}
                      </div>
                    </div>
                    <ChevronRight className="w-4 h-4 text-[#8b949e] group-hover:text-white" />
                  </div>
                ))
              ) : (
                <div className="text-center py-8 text-[#8b949e] text-xs">
                  <p>No saved projects yet.</p>
                  <p className="text-[11px] text-[#484f58] mt-1">
                    {isAuthenticated ? 'Create a new project from the IDE.' : 'Sign in to create persistent projects.'}
                  </p>
                </div>
              )}
            </div>
          </div>

          {/* Quick Start Templates */}
          <div className="bg-[#161b22] border border-[#21262d] rounded-lg p-4 flex flex-col">
            <div className="flex items-center justify-between mb-3 pb-2 border-b border-[#21262d]">
              <span className="text-xs font-semibold text-white flex items-center gap-1.5">
                <Sparkles className="w-4 h-4 text-amber-400" />
                Compiler Example Programs
              </span>
            </div>

            <div className="flex-1 space-y-2">
              {SAMPLE_PROGRAMS.slice(0, 4).map(sample => (
                <div
                  key={sample.id}
                  onClick={() => handleOpenTemplate(sample)}
                  className="p-3 bg-[#0d1117] hover:bg-[#21262d] rounded border border-[#21262d] cursor-pointer flex items-center justify-between transition-colors group"
                >
                  <div>
                    <div className="text-xs font-semibold text-amber-300 group-hover:text-amber-200">
                      {sample.name}
                    </div>
                    <div className="text-[11px] text-[#8b949e] mt-0.5 truncate max-w-sm">
                      {sample.description}
                    </div>
                  </div>
                  <ChevronRight className="w-4 h-4 text-[#8b949e] group-hover:text-white" />
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
};
