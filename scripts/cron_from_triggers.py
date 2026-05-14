#!/usr/bin/env python3
"""Print cron expression from triggers.yaml for a given stack and job type."""

from __future__ import annotations

import argparse
import sys
from pathlib import Path

try:
    import yaml
except ImportError as e:  # pragma: no cover
    print("Install PyYAML: pip install pyyaml", file=sys.stderr)
    raise SystemExit(1) from e


def resolve_cron(data: dict, stack: str, job_type: str) -> str:
    override = data.get("override") or {}
    per_stack = override.get(stack) or {}
    if job_type in per_stack and per_stack[job_type] is not None:
        return str(per_stack[job_type]).strip()
    triggers = data.get("triggers") or {}
    raw = triggers.get(job_type)
    return str(raw).strip() if raw is not None else ""


def main() -> int:
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("stack", help="Stack name (e.g. platform)")
    p.add_argument("jobtype", help="Job type key (e.g. smoke)")
    p.add_argument(
        "-f",
        "--file",
        type=Path,
        default=Path("triggers.yaml"),
        help="Path to triggers.yaml (default: ./triggers.yaml)",
    )
    args = p.parse_args()

    path = args.file
    if not path.is_file():
        print(f"Not found: {path}", file=sys.stderr)
        return 1

    with path.open(encoding="utf-8") as f:
        data = yaml.safe_load(f) or {}

    if not isinstance(data, dict):
        print("Invalid YAML: expected a mapping at root", file=sys.stderr)
        return 1

    cron = resolve_cron(data, args.stack, args.jobtype)
    print(cron)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
