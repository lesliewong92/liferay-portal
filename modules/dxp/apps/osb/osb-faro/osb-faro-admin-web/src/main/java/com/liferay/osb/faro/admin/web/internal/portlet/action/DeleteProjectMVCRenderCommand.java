/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.admin.web.internal.portlet.action;

import com.liferay.osb.faro.admin.web.internal.constants.FaroAdminPortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Marcos Martins
 */
@Component(
	property = {
		"javax.portlet.name=" + FaroAdminPortletKeys.FARO_ADMIN,
		"mvc.command.name=/faro_admin/delete_project_render"
	},
	service = MVCRenderCommand.class
)
public class DeleteProjectMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		return "/delete_project.jsp";
	}

}