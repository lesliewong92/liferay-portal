/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.jenkins.results.parser;

/**
 * @author Leslie Wong
 */
public abstract class BuildStatusEventListener implements EventListener {

    @Override
    public void update(Build build) {
        int currentSlaveUsageCount = build.getCurrentSlaveUsageCount();

        TopLevelBuild topLevelBuild = getTopLevelBuild();

        String topLevelJobName = topLevelBuild.getJobName();

        String batchName = build.getJobVariant();

        int x = batchName.indexOf("/");

        if (x != -1) {
            batchName = jobVariant.substring(0, x);
        }

        StringBuilder sb = new StringBuilder();

        sb.append("build.slave.usage.value:");
        sb.append(currentSlaveUsageCount);
        sb.append("|g");
        sb.append("|#top.level.job.name:");
        sb.append(topLevelJobName);
        sb.append(",batch.name:");
        sb.append(batchName);

        // build.slave.usage.value:currentSlaveUsageCount|#top.level.job.name:topLevelJobName,batch.name:batchName
    }

}