from mpgm.mpgm.model_fitters.prox_grad_fitters import Prox_Grad_Fitter
from mpgm.mpgm.evaluation.evaluation_metrics import *

from mpgm.mpgm.sample_generation.samplers import *
from mpgm.mpgm.sample_generation.graph_generators import *
from mpgm.mpgm.sample_generation.weight_assigners import *

# from typing import List, Any, Callable
from mpgm.mpgm.evaluation.evaluation import Experiment
from mpgm.mpgm.evaluation.evaluation import StatsGenerator
from mpgm.mpgm.evaluation.evaluation_metrics import EvalMetrics

from mpgm.mpgm.evaluation.preprocessing import ClampMax

import matplotlib.pyplot as plt

def vary_sparsity(SPW:SampleParamsWrapper, nr_edges:int):
    SPW.graph_generator = RandomNmGraphGenerator(nr_edges=nr_edges)

sparsities = []
edges_nrs = [5, 10, 15, 20, 25, 30, 35, 40]
for nr_edges in edges_nrs:
    gg = RandomNmGraphGenerator(nr_edges)
    G = gg.generate_graph(10)
    sparsity = EvalMetrics.calculate_percentage_sparsity(G) * 100
    sparsities.append(sparsity)

samples_file_name = "../samples.sqlite"
fit_file_name = "../fit_models.sqlite"

# Sparsity names:
experiment_name = "lattice_vary_sparsity_SIPRV_weight_normal"
experiment_name1 = "lattice_vary_sparsity_Gibbs_weight_normal"

nr_variables = 10
nr_samples = 150
alpha_SIPRV = 2
alpha_Gibbs = 0.2

if __name__ == '__main__':
    SGW = SampleParamsWrapper(nr_variables=nr_variables,
                              sample_init=np.zeros((nr_variables, )),
                              nr_samples=nr_samples,
                              random_seed=-1)

    SGW.graph_generator = LatticeGraphGenerator(sparsity_level=0)

    # SGW.weight_assigner = Dummy_Weight_Assigner()
    # SGW.sampler = SIPRVSampler(lambda_true=1, lambda_noise=0.5)

    SGW.weight_assigner = Constant_Weight_Assigner(ct_weight=-0.5)
    SGW.sampler = TPGMGibbsSampler(burn_in=200, thinning_nr=150)

    SGW.model = TPGM(R=10)

    nr_batches = len(edges_nrs)

    FPW = FitParamsWrapper(random_seed=0,
                           samples_file_name=samples_file_name)
    FPW.model = TPGM(R=10)
    FPW.fitter = Prox_Grad_Fitter(alpha=alpha_Gibbs,
                                  accelerated=True,
                                  save_regularization_paths=False,
                                  early_stop_criterion='weight',
                                  keep_diag_zero=False)
    FPW.preprocessor = ClampMax(10)

    theta_init = np.random.normal(0, 0.05, (nr_variables, nr_variables))
    theta_init[np.tril_indices(nr_variables)] = 0
    theta_init = theta_init + theta_init.T

    experiment1 = Experiment(experiment_name=experiment_name1,
                            random_seeds=list(range(5)),
                            SPW=SGW,
                            samples_file_name=samples_file_name,
                            FPW=FPW,
                            fit_file_name=fit_file_name,
                            vary_fit=False,
                            fit_theta_init=theta_init,
                            samples_name=experiment_name1
                            )

    experiment = Experiment(experiment_name=experiment_name,
                            random_seeds=list(range(5)),
                            SPW=SGW,
                            samples_file_name=samples_file_name,
                            FPW=FPW,
                            fit_file_name=fit_file_name,
                            vary_fit=False,
                            fit_theta_init=theta_init,
                            samples_name=experiment_name
                            )

    # experiment1.vary_x_generate_samples(edges_nrs, vary_sparsity)
    # experiment1.fit_all_samples_same_FPW(len(edges_nrs))

    stats_gen = StatsGenerator(experiments=[experiment, experiment1],
                               experiment_labels=['SIPRV', 'Gibbs'],
                               var_name="sample_sparsity",
                               var_values=sparsities,
                               folder_name=experiment_name)

    stats_gen.plot_ALL("vary_sparsity", "sample_sparsity", sparsities, x_log_scale=False,
                       SIPRV_active=False)
