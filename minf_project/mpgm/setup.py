from setuptools import setup


setup(
    name="mpgm",
    version="0.1",
    description="A package for fitting Multivariate Poisson Graphical Models",
    url="",
    license="None",
    packages=["mpgm"],
    install_requires=[
        "numpy",
        "networkx",
        "matplotlib",
        "tqdm",
    ],
    zip_safe=False,
)
